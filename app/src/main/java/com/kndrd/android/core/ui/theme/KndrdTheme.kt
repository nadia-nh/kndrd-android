package com.kndrd.android.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand colors
private val RoseMauve = Color(0xFFC2778A)
private val DeepRose = Color(0xFF9E5068)
private val SoftCream = Color(0xFFFDF6F0)
private val WarmTaupe = Color(0xFFE8D5C8)
private val DeepCharcoal = Color(0xFF2D2D2D)
private val SoftGray = Color(0xFF6B6B6B)

private val LightColors = lightColorScheme(
    primary = RoseMauve,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF5D6DE),
    onPrimaryContainer = Color(0xFF3D0018),
    secondary = Color(0xFF8B6573),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFD8E4),
    onSecondaryContainer = Color(0xFF3A1020),
    background = SoftCream,
    onBackground = DeepCharcoal,
    surface = Color.White,
    onSurface = DeepCharcoal,
    surfaceVariant = WarmTaupe,
    onSurfaceVariant = Color(0xFF534347),
    outline = Color(0xFF857377),
    error = Color(0xFFB3261E),
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB1C5),
    onPrimary = Color(0xFF5E1130),
    primaryContainer = DeepRose,
    onPrimaryContainer = Color(0xFFFFD9E3),
    secondary = Color(0xFFE8B9C7),
    onSecondary = Color(0xFF492535),
    secondaryContainer = Color(0xFF623B4C),
    onSecondaryContainer = Color(0xFFFFD8E4),
    background = Color(0xFF1A1114),
    onBackground = Color(0xFFF0DEE2),
    surface = Color(0xFF211719),
    onSurface = Color(0xFFF0DEE2),
    surfaceVariant = Color(0xFF534347),
    onSurfaceVariant = Color(0xFFD9BFC3),
    outline = Color(0xFFA2898E),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
)

@Composable
fun KndrdTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
