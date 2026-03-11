package com.nimbus.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.nimbus.data.db.AppDatabase
import com.nimbus.data.model.SessionEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// ────────────────────────────────────────────────────────────────
// Pomodoro modes grounded in cognitive science:
//  CLASSIC  → Cirillo (1992) + cognitive load theory: 25/5 cycles
//  FLOW     → DeskTime research + ultradian rhythms: 52/17 split
//  SPRINT   → CBT task-initiation: 15/3 to beat procrastination
// ────────────────────────────────────────────────────────────────

enum class PomodoroMode(
    val label: String,
    val emoji: String,
    val workMin: Int,
    val shortBreakMin: Int,
    val longBreakMin: Int,
    val cyclesBeforeLong: Int,
    val science: String
) {
    CLASSIC("Deep Work", "🎯", 25, 5, 15, 4,
        "Based on Cirillo's Pomodoro Technique — optimizes working memory via structured 25-min bursts"),
    FLOW("Flow Rhythm", "🌊", 52, 17, 20, 2,
        "Aligned with ultradian 90-min biological rhythms (Kleitman) and DeskTime productivity research"),
    SPRINT("Quick Sprint", "⚡", 15, 3, 10, 4,
        "CBT-based task initiation — short sprints lower activation threshold and beat procrastination")
}

enum class TimerPhase { WORK, SHORT_BREAK, LONG_BREAK }

data class PomodoroState(
    val mode: PomodoroMode = PomodoroMode.CLASSIC,
    val phase: TimerPhase = TimerPhase.WORK,
    val totalSeconds: Int = PomodoroMode.CLASSIC.workMin * 60,
    val remainingSeconds: Int = PomodoroMode.CLASSIC.workMin * 60,
    val isRunning: Boolean = false,
    val cycleCount: Int = 0,
    val completedSessions: Int = 0
)

class PomodoroViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).sessionDao()
    private var timerJob: Job? = null

    private val _state = MutableStateFlow(PomodoroState())
    val state: StateFlow<PomodoroState> = _state.asStateFlow()

    val progress: Float get() {
        val s = _state.value
        return if (s.totalSeconds == 0) 0f
        else 1f - (s.remainingSeconds.toFloat() / s.totalSeconds.toFloat())
    }

    fun selectMode(mode: PomodoroMode) {
        timerJob?.cancel()
        _state.value = PomodoroState(
            mode = mode,
            totalSeconds = mode.workMin * 60,
            remainingSeconds = mode.workMin * 60
        )
    }

    fun togglePlayPause() {
        val s = _state.value
        if (s.isRunning) {
            timerJob?.cancel()
            _state.value = s.copy(isRunning = false)
        } else {
            _state.value = s.copy(isRunning = true)
            startCountdown()
        }
    }

    fun reset() {
        timerJob?.cancel()
        val s = _state.value
        val seconds = phaseSeconds(s.mode, TimerPhase.WORK)
        _state.value = s.copy(isRunning = false, phase = TimerPhase.WORK,
            remainingSeconds = seconds, totalSeconds = seconds)
    }

    private fun startCountdown() {
        timerJob = viewModelScope.launch {
            while (_state.value.remainingSeconds > 0 && _state.value.isRunning) {
                delay(1000L)
                _state.value = _state.value.copy(remainingSeconds = _state.value.remainingSeconds - 1)
            }
            if (_state.value.remainingSeconds == 0) onPhaseComplete()
        }
    }

    private fun onPhaseComplete() {
        val s = _state.value
        if (s.phase == TimerPhase.WORK) {
            // Save completed focus session to Room DB
            viewModelScope.launch {
                dao.insert(SessionEntity(type = s.mode.name, durationMinutes = s.mode.workMin))
            }
        }
        val newCycle = if (s.phase == TimerPhase.WORK) s.cycleCount + 1 else s.cycleCount
        val nextPhase = nextPhase(s.mode, s.phase, newCycle)
        val nextSeconds = phaseSeconds(s.mode, nextPhase)
        _state.value = s.copy(
            phase = nextPhase, cycleCount = newCycle,
            remainingSeconds = nextSeconds, totalSeconds = nextSeconds,
            completedSessions = if (s.phase == TimerPhase.WORK) s.completedSessions + 1 else s.completedSessions,
            isRunning = true
        )
        startCountdown()
    }

    private fun nextPhase(mode: PomodoroMode, current: TimerPhase, cycle: Int): TimerPhase =
        when (current) {
            TimerPhase.WORK -> if (cycle % mode.cyclesBeforeLong == 0) TimerPhase.LONG_BREAK else TimerPhase.SHORT_BREAK
            else -> TimerPhase.WORK
        }

    private fun phaseSeconds(mode: PomodoroMode, phase: TimerPhase): Int =
        when (phase) {
            TimerPhase.WORK        -> mode.workMin * 60
            TimerPhase.SHORT_BREAK -> mode.shortBreakMin * 60
            TimerPhase.LONG_BREAK  -> mode.longBreakMin * 60
        }

    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }
}
