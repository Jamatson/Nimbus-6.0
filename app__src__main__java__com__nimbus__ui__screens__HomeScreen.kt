package com.nimbus.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nimbus.ui.components.TimerRing
import com.nimbus.ui.theme.*
import com.nimbus.viewmodel.*

@Composable
fun HomeScreen(vm: PomodoroViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
    ) {
        // Drifting cloud background shapes
        CloudBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                "nimbus",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = Comfortaa, color = DeepSky
                )
            )

            Spacer(Modifier.height(8.dp))

            // Mode selector
            ModeSelector(
                selected = state.mode,
                onSelect = { vm.selectMode(it) }
            )

            Spacer(Modifier.height(32.dp))

            // Timer ring - main focal point
            TimerRing(
                progress = vm.progress,
                timeLabel = vm.formatTime(state.remainingSeconds),
                phaseLabel = when (state.phase) {
                    TimerPhase.WORK        -> "Focus Time"
                    TimerPhase.SHORT_BREAK -> "Short Break ☁️"
                    TimerPhase.LONG_BREAK  -> "Long Rest 🌙"
                },
                ringColor = when (state.phase) {
                    TimerPhase.WORK        -> DeepSky
                    TimerPhase.SHORT_BREAK -> MintBreeze
                    TimerPhase.LONG_BREAK  -> CalmLavender
                },
                modifier = Modifier.size(280.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Cycle dots
            CycleDots(
                completed = state.cycleCount % state.mode.cyclesBeforeLong,
                total = state.mode.cyclesBeforeLong
            )

            Spacer(Modifier.height(32.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset
                OutlinedIconButton(
                    onClick = { vm.reset() },
                    modifier = Modifier.size(52.dp),
                    border = BorderStroke(1.5.dp, MistBlue),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Rounded.Refresh, "Reset", tint = DeepSky)
                }

                // Play/Pause — main CTA
                Button(
                    onClick = { vm.togglePlayPause() },
                    modifier = Modifier.height(56.dp).width(160.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepSky)
                ) {
                    Icon(
                        if (state.isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (state.isRunning) "Pause" else "Start",
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Science card
            ScienceCard(text = state.mode.science)

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ModeSelector(selected: PomodoroMode, onSelect: (PomodoroMode) -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MistBlue.copy(alpha = 0.5f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PomodoroMode.entries.forEach { mode ->
            val isSelected = mode == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) DeepSky else Color.Transparent)
                    .clickable { onSelect(mode) }
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${mode.emoji} ${mode.label}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = if (isSelected) Color.White else NightSky.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}

@Composable
private fun CycleDots(completed: Int, total: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(total) { i ->
            Box(
                modifier = Modifier
                    .size(if (i < completed) 10.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (i < completed) DeepSky else MistBlue)
            )
        }
    }
}

@Composable
private fun ScienceCard(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MistBlue.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Text("🔬", fontSize = 18.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = NightSky.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            )
        }
    }
}

@Composable
private fun CloudBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "clouds")
    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 30f,
        animationSpec = infiniteRepeatable(tween(18000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "c1"
    )
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -20f,
        animationSpec = infiniteRepeatable(tween(14000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "c2"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(color = SkyBlue.copy(alpha = 0.06f), radius = size.width * 0.55f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.15f + offset1, size.height * 0.12f))
        drawCircle(color = LavenderMist.copy(alpha = 0.18f), radius = size.width * 0.4f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.88f + offset2, size.height * 0.08f))
        drawCircle(color = MistBlue.copy(alpha = 0.12f), radius = size.width * 0.35f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.5f + offset1 * 0.5f, size.height * 0.92f))
    }
}
