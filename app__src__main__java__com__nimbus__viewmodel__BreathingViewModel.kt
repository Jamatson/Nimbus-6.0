package com.nimbus.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// ────────────────────────────────────────────────────────────────
// Breathing techniques — clinical psychology & neuroscience:
//  CALM    → 4-7-8 (Weil) — vagal nerve / parasympathetic activation
//  ENERGY  → Cyclic hyperventilation (Wim Hof / Kox et al. 2014)
//  ANXIETY → Box Breathing (Navy SEAL) + Physiological Sigh (Huberman/Stanford)
// ────────────────────────────────────────────────────────────────

enum class BreathingTechnique(
    val label: String,
    val emoji: String,
    val subtitle: String,
    val science: String
) {
    CALM("Calm", "😌", "4-7-8 Relaxation",
        "Activates parasympathetic nervous system via vagal nerve stimulation. Reduces cortisol (Ma et al., 2017)"),
    ENERGY("Energy", "⚡", "Cyclic Power Breath",
        "Increases adrenaline & alertness. Based on Wim Hof Method (Kox et al., 2014, PNAS)"),
    ANXIETY("Anxiety Relief", "💙", "Box Breath + Sigh",
        "Navy SEAL stress protocol + Stanford physiological sigh (Yackle et al., 2017) — fastest HR reduction known")
}

data class BreathPhase(val label: String, val durationSeconds: Int, val scale: Float)

data class BreathingState(
    val technique: BreathingTechnique = BreathingTechnique.CALM,
    val currentPhase: BreathPhase = BreathPhase("Ready", 0, 0.5f),
    val phaseProgress: Float = 0f,
    val currentRound: Int = 0,
    val totalRounds: Int = 4,
    val isRunning: Boolean = false,
    val isComplete: Boolean = false,
    val showEnergyWarning: Boolean = false
)

class BreathingViewModel : ViewModel() {

    private val _state = MutableStateFlow(BreathingState())
    val state: StateFlow<BreathingState> = _state.asStateFlow()

    private var breathJob: Job? = null

    // Phase sequences per technique
    private fun calmPhases(): List<BreathPhase> = listOf(
        BreathPhase("Breathe In", 4, 1.0f),
        BreathPhase("Hold...", 7, 1.0f),
        BreathPhase("Breathe Out", 8, 0.3f)
    )

    private fun energyPhases(powerBreathCount: Int = 30): List<BreathPhase> {
        val powerBreaths = List(powerBreathCount) { BreathPhase("Breathe In", 1, 1.0f) } +
                           List(powerBreathCount) { BreathPhase("Let Go", 1, 0.4f) }
        return powerBreaths + listOf(
            BreathPhase("Hold Empty", 20, 0.3f),
            BreathPhase("Big Breath In", 3, 1.0f),
            BreathPhase("Hold Full", 15, 1.0f)
        )
    }

    private fun anxietyPhases(): List<BreathPhase> = listOf(
        // Physiological sigh intro
        BreathPhase("Sniff In", 2, 0.85f),
        BreathPhase("Top Off", 1, 1.0f),
        BreathPhase("Long Exhale", 6, 0.2f),
        // Box breathing x5
        BreathPhase("Inhale", 4, 1.0f),
        BreathPhase("Hold", 4, 1.0f),
        BreathPhase("Exhale", 4, 0.2f),
        BreathPhase("Hold", 4, 0.2f),
        BreathPhase("Inhale", 4, 1.0f),
        BreathPhase("Hold", 4, 1.0f),
        BreathPhase("Exhale", 4, 0.2f),
        BreathPhase("Hold", 4, 0.2f),
        BreathPhase("Inhale", 4, 1.0f),
        BreathPhase("Hold", 4, 1.0f),
        BreathPhase("Exhale", 4, 0.2f),
        BreathPhase("Hold", 4, 0.2f),
        BreathPhase("Inhale", 4, 1.0f),
        BreathPhase("Hold", 4, 1.0f),
        BreathPhase("Exhale", 4, 0.2f),
        BreathPhase("Hold", 4, 0.2f),
        BreathPhase("Inhale", 4, 1.0f),
        BreathPhase("Hold", 4, 1.0f),
        BreathPhase("Exhale", 4, 0.2f),
        BreathPhase("Hold", 4, 0.2f),
    )

    fun selectTechnique(technique: BreathingTechnique) {
        breathJob?.cancel()
        val showWarning = technique == BreathingTechnique.ENERGY
        _state.value = BreathingState(technique = technique, showEnergyWarning = showWarning)
    }

    fun dismissEnergyWarning() {
        _state.value = _state.value.copy(showEnergyWarning = false)
    }

    fun startSession() {
        breathJob?.cancel()
        _state.value = _state.value.copy(isRunning = true, isComplete = false, currentRound = 1)
        breathJob = viewModelScope.launch { runSession() }
    }

    fun stop() {
        breathJob?.cancel()
        _state.value = _state.value.copy(isRunning = false)
    }

    private suspend fun runSession() {
        val phases = when (_state.value.technique) {
            BreathingTechnique.CALM   -> calmPhases()
            BreathingTechnique.ENERGY -> energyPhases()
            BreathingTechnique.ANXIETY -> anxietyPhases()
        }
        val rounds = when (_state.value.technique) {
            BreathingTechnique.CALM    -> 4
            BreathingTechnique.ENERGY  -> 2
            BreathingTechnique.ANXIETY -> 1
        }

        repeat(rounds) { round ->
            if (!_state.value.isRunning) return
            _state.value = _state.value.copy(currentRound = round + 1, totalRounds = rounds)
            for (phase in phases) {
                if (!_state.value.isRunning) return
                _state.value = _state.value.copy(currentPhase = phase, phaseProgress = 0f)
                val totalMs = phase.durationSeconds * 1000L
                val tickMs = 50L
                val ticks = totalMs / tickMs
                repeat(ticks.toInt()) { tick ->
                    if (!_state.value.isRunning) return
                    delay(tickMs)
                    _state.value = _state.value.copy(phaseProgress = (tick + 1f) / ticks)
                }
            }
        }
        _state.value = _state.value.copy(isRunning = false, isComplete = true,
            currentPhase = BreathPhase("Complete ✨", 0, 0.6f))
    }
}
