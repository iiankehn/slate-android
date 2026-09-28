package com.iiankehn.slate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CoreBlue = Color(0xFF0072BC)
val Midnight = Color(0xFF090B0F)
val SlateSurface = Color(0xFF11151C)
val SlateSurfaceRaised = Color(0xFF1A2029)
val SlateText = Color(0xFFF5F8FC)
val SlateTextMuted = Color(0xFF9DA9B7)

private val SlateColors = darkColorScheme(
    primary = CoreBlue,
    onPrimary = Color.White,
    background = Midnight,
    onBackground = SlateText,
    surface = SlateSurface,
    onSurface = SlateText,
    surfaceVariant = SlateSurfaceRaised,
    onSurfaceVariant = SlateTextMuted,
    outline = Color(0xFF33404F),
)

@Composable
fun SlateTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SlateColors,
        content = content,
    )
}
