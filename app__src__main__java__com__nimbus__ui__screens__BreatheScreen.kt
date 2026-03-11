@file:OptIn(ExperimentalMaterial3Api::class)
package com.nimbus.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nimbus.ui.components.BreathingOrb
import com.nimbus.ui.theme.*
import com.nimbus.viewmodel.*

@Composable
fun BreatheScreen(vm: BreathingViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()

    // Energy safety warning dialog
    if (state.showEnergyWarning) {
        AlertDialog(
            onDismissRequest = { vm.dismissEnergyWarning() },
            shape = RoundedCornerShape(24.dp),
            containerColor = CloudWhite,
            icon = { Text("⚠️", fontSize = 28.sp) },
            title = { Text("Safety Notice", fontFamily = Comfortaa, color = NightSky) },
            text = {
                Text(
                    "Do not practice while driving, in water, or standing up. " +
                    "Stop immediately if you feel lightheaded or dizzy. " +
                    "Not suitable during pregnancy.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = NightSky.copy(alpha = 0.75f))
                )
            },
            confirmButton = {
                Button(
                    onClick = { vm.dismissEnergyWarning() },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepSky),
                    shape = RoundedCornerShape(16.dp)
                ) { Text("I Understand") }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().background(CloudWhite),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Text("breathe",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = Comfortaa, color = DeepSky
                )
            )

            Spacer(Modifier.height(20.dp))

            // Technique selector chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                BreathingTechnique.entries.forEach { technique ->
                    val isSelected = technique == state.technique
                    val chipColor = when (technique) {
                        BreathingTechnique.CALM    -> CalmLavender
                        BreathingTechnique.ENERGY  -> SoftGold.copy(alpha = 0.6f)
                        BreathingTechnique.ANXIETY -> SkyBlue
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = { if (!state.isRunning) vm.selectTechnique(technique) },
                        label = {
                            Text("${technique.emoji} ${technique.label}",
                                style = MaterialTheme.typography.labelMedium)
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (isSelected) DeepSky else chipColor,
                            selectedLabelColor = if (isSelected) Color.White else NightSky
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Subtitle
            Text(
                state.technique.subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = NightSky.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(36.dp))

            // Breathing orb - the main visual
            val orbColor = when (state.technique) {
                BreathingTechnique.CALM    -> CalmLavender
                BreathingTechnique.ENERGY  -> SoftGold.copy(red = 1f, green = 0.88f, blue = 0.4f)
                BreathingTechnique.ANXIETY -> SkyBlue
            }

            BreathingOrb(
                targetScale = if (state.isRunning) state.currentPhase.scale else 0.5f,
                phaseLabel = if (state.isRunning) state.currentPhase.label else "Tap to begin",
                orbColor = orbColor,
                modifier = Modifier.size(260.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Round counter
            if (state.isRunning && state.totalRounds > 1) {
                Text(
                    "Round ${state.currentRound} of ${state.totalRounds}",
                    style = MaterialTheme.typography.labelLarge.copy(color = DeepSky.copy(alpha = 0.7f))
                )
                Spacer(Modifier.height(12.dp))
            }

            if (state.isComplete) {
                Text("✨ Session complete. Well done.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MintBreeze, textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.height(16.dp))
            }

            // Start / Stop button
            Button(
                onClick = { if (state.isRunning) vm.stop() else vm.startSession() },
                modifier = Modifier.height(56.dp).width(180.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isRunning) SoftRose.copy(alpha = 0.8f) else DeepSky
                )
            ) {
                Icon(
                    if (state.isRunning) Icons.Rounded.Stop else Icons.Rounded.Air,
                    contentDescription = null,
                    tint = if (state.isRunning) Color(0xFFB03030) else Color.White
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    if (state.isRunning) "Stop" else "Begin",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (state.isRunning) Color(0xFFB03030) else Color.White
                    )
                )
            }

            Spacer(Modifier.height(28.dp))

            // Science explanation card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MistBlue.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Text("🧠", fontSize = 18.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        state.technique.science,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NightSky.copy(alpha = 0.65f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
