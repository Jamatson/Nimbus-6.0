package com.nimbus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp

// Using system fonts — clean, reliable, no GMS dependency needed
val Comfortaa = FontFamily.SansSerif
val Nunito    = FontFamily.SansSerif

val NimbusTypography = Typography(
    displayLarge   = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold,     fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium  = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold,     fontSize = 45.sp, lineHeight = 52.sp),
    headlineLarge  = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold,     fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold,     fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall  = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium,   fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge     = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold,     fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium    = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),
    bodyLarge      = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium     = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall      = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge     = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    labelMedium    = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium,   fontSize = 12.sp),
    labelSmall     = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium,   fontSize = 11.sp),
)
