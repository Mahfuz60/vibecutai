package com.vibecut.ai.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// VibeCut AI Professional Color System
object VibeCutColors {
    val Background = Color(0xFF000000)      // Pure Black
    val Surface = Color(0xFF121212)         // Dark Grey
    val SurfaceVariant = Color(0xFF1E1E1E)  // Lighter Dark Grey
    val Primary = Color(0xFFBB86FC)         // Light Purple
    val Secondary = Color(0xFF03DAC6)       // Teal
    val Accent = Color(0xFFFF4081)          // Pink Accent
    val OnBackground = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFFE1E1E1)
    val Error = Color(0xFFCF6679)
    
    // Timeline colors from reference
    val TimelineTrack1 = Color(0xFF4A1F8A)
    val TimelineTrack2 = Color(0xFF6A007F)
    val TimelineTrack3 = Color(0xFF00E5FF)
}

val VibeCutShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

val VibeCutTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = VibeCutColors.Primary,
    onPrimary = Color.Black,
    secondary = VibeCutColors.Secondary,
    onSecondary = Color.Black,
    tertiary = VibeCutColors.Accent,
    background = VibeCutColors.Background,
    onBackground = VibeCutColors.OnBackground,
    surface = VibeCutColors.Surface,
    onSurface = VibeCutColors.OnSurface,
    surfaceVariant = VibeCutColors.SurfaceVariant,
    error = VibeCutColors.Error,
)

@Composable
fun VibeCutTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
            window.navigationBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = VibeCutTypography,
        shapes = VibeCutShapes,
        content = content
    )
}
