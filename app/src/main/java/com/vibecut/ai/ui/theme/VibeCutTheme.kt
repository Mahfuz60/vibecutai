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

// VibeCut AI Cyberpunk/Neon Professional Color System
object VibeCutColors {
    val Background = Color(0xFF050505)      // Deep Black
    val Surface = Color(0xFF0D1117)         // Deep Dark Blue/Grey
    val SurfaceVariant = Color(0xFF161B22)  // Lighter Dark
    
    val Primary = Color(0xFF00F0FF)         // Neon Cyan (from reference)
    val Secondary = Color(0xFFFF003C)       // Neon Red/Pink (from reference)
    val Accent = Color(0xFF7000FF)          // Cyber Purple
    
    val OnBackground = Color(0xFFE6EDF3)
    val OnSurface = Color(0xFFC9D1D9)
    val Error = Color(0xFFF85149)
    
    val NeonCyan = Color(0xFF00F0FF)
    val NeonRed = Color(0xFFFF003C)
    
    // Timeline colors matching the neon aesthetic
    val TimelineTrack1 = Color(0xFF1F6FEB)
    val TimelineTrack2 = Color(0xFFAB7DF8)
    val TimelineTrack3 = Color(0xFF238636)
}

val VibeCutShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp)
)

val VibeCutTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
        letterSpacing = (-1).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = VibeCutColors.Primary,
    onPrimary = Color.Black,
    secondary = VibeCutColors.Secondary,
    onSecondary = Color.White,
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
            window.statusBarColor = VibeCutColors.Background.toArgb()
            window.navigationBarColor = VibeCutColors.Background.toArgb()
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
