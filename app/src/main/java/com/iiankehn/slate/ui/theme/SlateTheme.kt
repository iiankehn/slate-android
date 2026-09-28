package com.iiankehn.slate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CoreBlue = Color(0xFF0072BC)
val CoreBlueLight = Color(0xFF72C7FF)
val Midnight = Color(0xFF0B0F14)
val SlateSurface = Color(0xFF111820)
val SlateSurfaceRaised = Color(0xFF1A232D)
val SlateText = Color(0xFFE7EDF4)
val SlateTextMuted = Color(0xFFA9B4C0)

private val SlateColors = darkColorScheme(
    primary = CoreBlueLight,
    onPrimary = Color(0xFF00344F),
    primaryContainer = Color(0xFF004B73),
    onPrimaryContainer = Color(0xFFCBE6FF),
    secondary = Color(0xFFB8C8D8),
    onSecondary = Color(0xFF23323F),
    secondaryContainer = Color(0xFF344956),
    onSecondaryContainer = Color(0xFFD4E5F5),
    background = Midnight,
    onBackground = SlateText,
    surface = SlateSurface,
    onSurface = SlateText,
    surfaceVariant = SlateSurfaceRaised,
    onSurfaceVariant = SlateTextMuted,
    surfaceContainer = Color(0xFF151D26),
    surfaceContainerHigh = Color(0xFF1B2530),
    surfaceContainerHighest = Color(0xFF232E3A),
    outline = Color(0xFF43515E),
    outlineVariant = Color(0xFF2A3540),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    scrim = Color.Black,
)

private val SlateTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 27.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
)

@Composable
fun SlateTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SlateColors,
        typography = SlateTypography,
        content = content,
    )
}
