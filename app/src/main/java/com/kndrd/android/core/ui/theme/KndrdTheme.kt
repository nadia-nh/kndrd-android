package com.kndrd.android.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Brand palette ─────────────────────────────────────────────────────────────
// Primary: deep plum-charcoal — dark, editorial (matches iOS), warm rosy undertone
private val PlumCharcoal      = Color(0xFF2C2028)
private val PlumCharcoalLight = Color(0xFF4A3842)

// Secondary / accent: rose-mauve — the "pink twist" on chips, links, selected states
private val RoseMauve         = Color(0xFFC2778A)
private val DeepRose          = Color(0xFF9E5068)

// Surfaces: blush-cream parchment — closer to iOS warm neutral, hint of rose
private val BlushCream        = Color(0xFFF5EAE4)   // background
private val BlushSurface      = Color(0xFFFAF3EF)   // card surfaces
private val BlushVariant      = Color(0xFFEDD8D0)   // chip fills, variants

// Text
private val DeepCharcoal      = Color(0xFF2D2D2D)

// ── Light scheme ──────────────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary              = PlumCharcoal,
    onPrimary            = Color.White,
    primaryContainer     = Color(0xFFEDD0D8),   // soft blush container
    onPrimaryContainer   = Color(0xFF1A0A12),   // near-black plum

    secondary            = RoseMauve,
    onSecondary          = Color.White,
    secondaryContainer   = Color(0xFFF5D6DE),   // light blush chip fill
    onSecondaryContainer = Color(0xFF3D0018),

    background           = BlushCream,
    onBackground         = DeepCharcoal,

    surface              = BlushSurface,
    onSurface            = DeepCharcoal,
    surfaceVariant       = BlushVariant,
    onSurfaceVariant     = Color(0xFF5A4448),

    outline              = Color(0xFF9E8589),

    error                = Color(0xFFB3261E),
    onError              = Color.White,
    errorContainer       = Color(0xFFF9DEDC),
    onErrorContainer     = Color(0xFF410E0B),
)

// ── Dark scheme ───────────────────────────────────────────────────────────────
private val DarkColors = darkColorScheme(
    primary              = Color(0xFFE8B5C3),   // light rose on dark
    onPrimary            = Color(0xFF47101F),
    primaryContainer     = Color(0xFF621728),
    onPrimaryContainer   = Color(0xFFFFD9E3),

    secondary            = Color(0xFFE8B5C3),
    onSecondary          = Color(0xFF492535),
    secondaryContainer   = Color(0xFF623B4C),
    onSecondaryContainer = Color(0xFFFFD8E4),

    background           = Color(0xFF1A1015),   // dark plum-black
    onBackground         = Color(0xFFF0DEE2),

    surface              = Color(0xFF221520),
    onSurface            = Color(0xFFF0DEE2),
    surfaceVariant       = Color(0xFF534347),
    onSurfaceVariant     = Color(0xFFD9BFC3),

    outline              = Color(0xFFA2898E),

    error                = Color(0xFFF2B8B5),
    onError              = Color(0xFF601410),
)

@Composable
fun KndrdTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
