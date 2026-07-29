package com.halaqa.quran.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryGold,
    background = BackgroundCream,
    surface = SurfaceWhite,
    onPrimary = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = PrimaryGreenLight,
    secondary = SecondaryGold,
    background = Color(0xFF10201A),
    surface = Color(0xFF1B2B22),
    onPrimary = TextDark,
    onBackground = SurfaceWhite,
    onSurface = SurfaceWhite,
    error = ErrorRed
)

/** فرض اتجاه RTL على كامل التطبيق بصرف النظر عن لغة النظام، لأن التطبيق عربي بالكامل */
@Composable
fun QuranHalaqaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content
        )
    }
}
