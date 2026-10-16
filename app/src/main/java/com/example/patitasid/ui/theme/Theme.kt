package com.example.patitasid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PetMateColors = lightColorScheme(
    primary = PetBlue,
    onPrimary = Color.White,
    secondary = PetOrange,
    onSecondary = PetOnOrange,
    background = PetBackground,
    onBackground = PetText,
    surface = PetSurface,
    onSurface = PetText,
    error = PetError
)

@Composable
fun PetMateTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PetMateColors, typography = Typography, content = content)
}
