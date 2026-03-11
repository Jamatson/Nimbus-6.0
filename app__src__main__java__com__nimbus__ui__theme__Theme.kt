package com.nimbus.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ☁️ Nimbus Cloud Palette
val CloudWhite    = Color(0xFFF8F9FF)
val MistBlue      = Color(0xFFD6E4F7)
val LavenderMist  = Color(0xFFE8E4F5)
val WarmPearl     = Color(0xFFFDF6F0)
val SkyBlue       = Color(0xFFA8C8E8)
val DeepSky       = Color(0xFF4A7FCB)
val NightSky      = Color(0xFF1A2540)
val SoftGold      = Color(0xFFFFF3C4)
val MintBreeze    = Color(0xFF3ECFA0)
val CalmLavender  = Color(0xFFB8A8E8)
val SoftRose      = Color(0xFFF7D6D6)

private val LightColorScheme = lightColorScheme(
    primary       = DeepSky,
    onPrimary     = CloudWhite,
    primaryContainer   = MistBlue,
    onPrimaryContainer = NightSky,
    secondary     = CalmLavender,
    onSecondary   = NightSky,
    secondaryContainer = LavenderMist,
    background    = CloudWhite,
    onBackground  = NightSky,
    surface       = CloudWhite,
    onSurface     = NightSky,
    surfaceVariant = MistBlue,
    outline       = SkyBlue,
)

@Composable
fun NimbusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = NimbusTypography,
        content     = content
    )
}
