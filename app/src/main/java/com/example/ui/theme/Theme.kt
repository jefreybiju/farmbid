package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Farmer Light Color Scheme based on user's tractor palette:
// #1b3f30, #385c4d, #669854, #5a5b5c, #8c4951
val FarmerLightColorScheme = lightColorScheme(
    primary = FarmerDarkGreen,          // #1b3f30
    onPrimary = Color.White,
    primaryContainer = FarmerContainerLight,
    onPrimaryContainer = FarmerDarkGreen,
    secondary = FarmerPineGreen,        // #385c4d
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE7E1),
    onSecondaryContainer = Color(0xFF162D22),
    tertiary = FarmerLeafGreen,         // #669854
    onTertiary = Color.White,
    tertiaryContainer = FarmerLeafContainer,
    onTertiaryContainer = Color(0xFF1E3A15),
    error = FarmerEarthRed,             // #8c4951
    onError = Color.White,
    errorContainer = FarmerRedContainer,
    onErrorContainer = Color(0xFF451318),
    background = FarmerLightBg,
    onBackground = FarmerTextPrimary,
    surface = FarmerSurface,
    onSurface = FarmerTextPrimary,
    surfaceVariant = FarmerSurfaceVariant,
    onSurfaceVariant = FarmerTextMuted,
    outline = FarmerSlateGray,          // #5a5b5c
    outlineVariant = FarmerOutline
)

val FarmerDarkColorScheme = darkColorScheme(
    primary = Color(0xFF669854),
    onPrimary = Color(0xFF0C2417),
    primaryContainer = FarmerDarkGreen,
    onPrimaryContainer = Color(0xFFD4E7DC),
    secondary = Color(0xFF86AB97),
    onSecondary = Color(0xFF152A20),
    secondaryContainer = FarmerPineGreen,
    onSecondaryContainer = Color(0xFFD0DFD6),
    tertiary = Color(0xFF90C27E),
    onTertiary = Color(0xFF1A3511),
    error = Color(0xFFE28B93),
    onError = Color(0xFF461118),
    background = Color(0xFF0F1A14),
    onBackground = Color(0xFFE3EBE5),
    surface = Color(0xFF14241B),
    onSurface = Color(0xFFE3EBE5),
    surfaceVariant = Color(0xFF1C2F24),
    onSurfaceVariant = Color(0xFFA5B8AD),
    outline = Color(0xFF6B7E72)
)

// Buyer Color Scheme
val BuyerLightColorScheme = lightColorScheme(
    primary = BuyerNavy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = BuyerNavy,
    secondary = BuyerBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0E7FF),
    onSecondaryContainer = Color(0xFF1E1B4B),
    tertiary = BuyerAmber,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF78350F),
    error = BuyerRed,
    onError = Color.White,
    background = BuyerLightBg,
    onBackground = BuyerTextPrimary,
    surface = BuyerSurface,
    onSurface = BuyerTextPrimary,
    surfaceVariant = BuyerSurfaceVariant,
    onSurfaceVariant = BuyerTextMuted,
    outline = BuyerSlate,
    outlineVariant = BuyerOutline
)

val BuyerDarkColorScheme = darkColorScheme(
    primary = Color(0xFF93C5FD),
    onPrimary = Color(0xFF1E3A8A),
    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF60A5FA),
    onSecondary = Color(0xFF172554),
    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF451A03),
    background = Color(0xFF0B1324),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF131F37),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF64748B)
)

@Composable
fun FarmBidTheme(
    isFarmerTheme: Boolean = true,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isFarmerTheme && darkTheme -> FarmerDarkColorScheme
        isFarmerTheme -> FarmerLightColorScheme
        !isFarmerTheme && darkTheme -> BuyerDarkColorScheme
        else -> BuyerLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
