package com.example.patitasid.ui.feature.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.patitasid.ui.components.PetPrimaryButton
import com.example.patitasid.ui.theme.PetMateTheme

// Figma frame 01 · Bienvenida, trimmed to current scope (no sign-up/login yet).
@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primary)
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier.size(88.dp).background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Pets, contentDescription = null, tint = colors.primary, modifier = Modifier.size(40.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("PetMate", style = MaterialTheme.typography.headlineLarge, color = colors.onPrimary)
        Text("El carnet digital de tu mascota", style = MaterialTheme.typography.bodyLarge, color = colors.onPrimary.copy(alpha = 0.85f))
        Spacer(Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PetPrimaryButton(text = "Comenzar", onClick = onStart)
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    PetMateTheme { WelcomeScreen(onStart = {}) }
}
