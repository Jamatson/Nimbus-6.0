package com.nimbus.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.nimbus.ui.theme.*

@Composable
fun StatsScreen() {
    // Placeholder stats — wire to Room DB via ViewModel for production
    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val mockMinutes = listOf(50, 75, 25, 100, 52, 0, 30)
    val maxMin = mockMinutes.max().coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudWhite)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Text("your week",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = Comfortaa, color = DeepSky
            )
        )

        Spacer(Modifier.height(8.dp))

        Text("Focus minutes tracked",
            style = MaterialTheme.typography.bodyMedium.copy(color = NightSky.copy(alpha = 0.5f))
        )

        Spacer(Modifier.height(32.dp))

        // Bar chart
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MistBlue.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weekDays.forEachIndexed { i, day ->
                    val mins = mockMinutes[i]
                    val heightFraction = mins.toFloat() / maxMin
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.height(160.dp)
                    ) {
                        if (mins > 0) {
                            Text(
                                "$mins",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DeepSky, fontSize = 9.sp
                                )
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                        Spacer(Modifier.weight(1f - heightFraction.coerceIn(0f, 0.95f)))
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .fillMaxHeight(heightFraction.coerceAtLeast(0.03f))
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(if (mins > 0) DeepSky.copy(alpha = 0.75f) else MistBlue)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(day, style = MaterialTheme.typography.labelSmall.copy(
                            color = NightSky.copy(alpha = 0.5f), fontSize = 10.sp
                        ))
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Summary cards
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("332", "Total mins", "🎯", Modifier.weight(1f))
            StatCard("12", "Sessions", "✅", Modifier.weight(1f))
            StatCard("5", "Day streak", "🔥", Modifier.weight(1f))
        }

        Spacer(Modifier.height(24.dp))

        // Mode breakdown
        Text("Mode breakdown",
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = Comfortaa, color = NightSky
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        listOf(
            Triple("🎯 Deep Work (25 min)", 0.6f, DeepSky),
            Triple("🌊 Flow Rhythm (52 min)", 0.25f, CalmLavender),
            Triple("⚡ Quick Sprint (15 min)", 0.15f, MintBreeze)
        ).forEach { (label, fraction, color) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
            ) {
                Text(label,
                    style = MaterialTheme.typography.bodySmall.copy(color = NightSky.copy(alpha = 0.7f)),
                    modifier = Modifier.width(180.dp)
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MistBlue)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(color)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun StatCard(value: String, label: String, emoji: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MistBlue.copy(alpha = 0.4f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = Comfortaa, color = NightSky
            ))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(
                color = NightSky.copy(alpha = 0.5f)
            ))
        }
    }
}
