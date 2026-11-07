package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SpiderDarkColorScheme = darkColorScheme(
    primary = SpiderRed,
    onPrimary = Color.White,
    primaryContainer = SpiderRedDark,
    onPrimaryContainer = SpiderWhite,
    secondary = SpiderRedLight,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = SpiderWhite,
    tertiary = Color(0xFFFF5252),
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkOutline,
    outlineVariant = DarkOutline.copy(alpha = 0.5f),
    error = SignalRed,
    onError = Color.White
)

@Composable
fun SpiderNetworkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SpiderDarkColorScheme,
        typography = Typography,
        content = content
    )
}
