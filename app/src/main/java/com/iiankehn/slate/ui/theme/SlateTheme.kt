package com.iiankehn.slate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CoreBlue = Color(0xFF0072BC)
val CoreBlueLight = Color(0xFF72C7FF)
val Midnight = Color(0xFF080C11)
val SlateSurface = Color(0xFF10161D)
val SlateSurfaceSoft = Color(0xFF151D26)
val SlateSurfaceRaised = Color(0xFF1B2530)
val SlateBorder = Color(0xFF2A3744)
val SlateText = Color(0xFFF1F5F9)
val SlateTextMuted = Color(0xFFA7B4C2)
data class SlateEditorColors(
    val canvas: Color,
    val paper: Color,
    val onPaper: Color,
    val paperOutline: Color,
)

private val LightEditorColors = SlateEditorColors(
    canvas = Color(0xFFE3E7EC),
    paper = Color(0xFFFFFBFF),
    onPaper = Color(0xFF1A1C1E),
    paperOutline = Color(0xFFCAD3DF),
)

private val DarkEditorColors = SlateEditorColors(
    canvas = Color(0xFF0B1016),
    paper = Color(0xFF171D24),
    onPaper = Color(0xFFE8EDF3),
    paperOutline = Color(0xFF465462),
)

private val LocalSlateEditorColors = staticCompositionLocalOf { LightEditorColors }

object SlateEditorTheme {
    val colors: SlateEditorColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSlateEditorColors.current
}

private val SlateLightColors = lightColorScheme(
    primary = CoreBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E9FF),
    onPrimaryContainer = Color(0xFF001D33),
    secondary = Color(0xFF52616F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6E5F4),
    onSecondaryContainer = Color(0xFF0E1D29),
    background = Color(0xFFF6F8FA),
    onBackground = Color(0xFF191C1E),
    surface = Color(0xFFFCFCFF),
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFDDE3EA),
    onSurfaceVariant = Color(0xFF41484D),
    surfaceContainer = Color(0xFFEEF2F5),
    surfaceContainerHigh = Color(0xFFE7EBEF),
    surfaceContainerHighest = Color(0xFFDFE4E8),
    outline = Color(0xFF72787E),
    outlineVariant = Color(0xFFC1C7CD),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    scrim = Color.Black,
)

private val SlateDarkColors = darkColorScheme(
    primary = CoreBlueLight,
    onPrimary = Color(0xFF003354),
    primaryContainer = Color(0xFF004B75),
    onPrimaryContainer = Color(0xFFD1E9FF),
    secondary = Color(0xFFB9C8D8),
    onSecondary = Color(0xFF24323F),
    secondaryContainer = Color(0xFF3A4856),
    onSecondaryContainer = Color(0xFFD6E5F4),
    background = Midnight,
    onBackground = SlateText,
    surface = SlateSurface,
    onSurface = SlateText,
    surfaceVariant = SlateSurfaceRaised,
    onSurfaceVariant = SlateTextMuted,
    surfaceContainer = SlateSurfaceSoft,
    surfaceContainerHigh = SlateSurfaceRaised,
    surfaceContainerHighest = Color(0xFF22303C),
    outline = Color(0xFF8B98A5),
    outlineVariant = SlateBorder,
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
        lineHeight = 28.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.sp,
    ),
)

private val SlateShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp),
)

internal fun slateEditorColors(darkTheme: Boolean): SlateEditorColors =
    if (darkTheme) DarkEditorColors else LightEditorColors

@Composable
fun SlateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSlateEditorColors provides slateEditorColors(darkTheme)) {
        MaterialTheme(
            colorScheme = if (darkTheme) SlateDarkColors else SlateLightColors,
            typography = SlateTypography,
            shapes = SlateShapes,
            content = content,
        )
    }
}
