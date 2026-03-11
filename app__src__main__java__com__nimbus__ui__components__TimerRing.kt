package com.nimbus.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.*
import com.nimbus.ui.theme.*

@Composable
fun TimerRing(
    progress: Float,
    timeLabel: String,
    phaseLabel: String,
    modifier: Modifier = Modifier,
    ringColor: Color = DeepSky,
    trackColor: Color = MistBlue
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic),
        label = "timerProgress"
    )

    // Subtle pulse on the ring glow
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "glowAlpha"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = size.minDimension * 0.06f
            val radius = (size.minDimension / 2f) - strokeWidth
            val center = this.center

            // Outer glow
            drawCircle(
                color = ringColor.copy(alpha = glowAlpha * 0.15f),
                radius = radius + strokeWidth * 1.5f,
                center = center
            )

            // Track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(ringColor.copy(alpha = 0.6f), ringColor, ringColor),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // End dot
            if (animatedProgress > 0.02f) {
                val angle = Math.toRadians((-90f + 360f * animatedProgress).toDouble())
                val dotX = (center.x + radius * kotlin.math.cos(angle)).toFloat()
                val dotY = (center.y + radius * kotlin.math.sin(angle)).toFloat()
                drawCircle(color = Color.White, radius = strokeWidth * 0.6f,
                    center = androidx.compose.ui.geometry.Offset(dotX, dotY))
                drawCircle(color = ringColor, radius = strokeWidth * 0.35f,
                    center = androidx.compose.ui.geometry.Offset(dotX, dotY))
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = timeLabel,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = com.nimbus.ui.theme.Comfortaa,
                    color = NightSky
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = phaseLabel,
                style = MaterialTheme.typography.labelLarge.copy(color = DeepSky.copy(alpha = 0.7f))
            )
        }
    }
}
