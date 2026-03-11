package com.nimbus.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.nimbus.ui.theme.*

@Composable
fun BreathingOrb(
    targetScale: Float,        // 0f = fully contracted, 1f = fully expanded
    phaseLabel: String,
    orbColor: Color = CalmLavender,
    modifier: Modifier = Modifier
) {
    // Smooth spring animation — asymmetric easing mimics real breath rhythm
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "orbScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "orbPulse")
    val glowRadius by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(3000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "glowRadius"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val baseRadius = size.minDimension * 0.28f
            val scaledRadius = baseRadius * (0.45f + animatedScale * 0.55f)
            val c = this.center

            // Outer glow halo
            drawCircle(
                color = orbColor.copy(alpha = 0.08f),
                radius = scaledRadius * 1.6f * glowRadius,
                center = c
            )
            drawCircle(
                color = orbColor.copy(alpha = 0.14f),
                radius = scaledRadius * 1.3f,
                center = c
            )

            // Main orb with gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.9f), orbColor.copy(alpha = 0.85f), orbColor),
                    center = c,
                    radius = scaledRadius
                ),
                radius = scaledRadius,
                center = c
            )

            // Highlight shimmer
            drawCircle(
                color = Color.White.copy(alpha = 0.35f),
                radius = scaledRadius * 0.38f,
                center = androidx.compose.ui.geometry.Offset(c.x - scaledRadius * 0.22f, c.y - scaledRadius * 0.22f)
            )
        }

        Text(
            text = phaseLabel,
            style = MaterialTheme.typography.titleMedium.copy(
                color = NightSky.copy(alpha = 0.75f),
                fontFamily = Nunito
            )
        )
    }
}
