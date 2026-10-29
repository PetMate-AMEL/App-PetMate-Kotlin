package com.example.patitasid.ui.feature.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.patitasid.ui.components.PetOutlinedButton
import com.example.patitasid.ui.components.PetPrimaryButton
import com.example.patitasid.ui.theme.PetMateTheme

// HU-008: the welcome screen only appears when there is no saved session (RF-01).
@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onTryDemo: () -> Unit,
    onForgotPassword: () -> Unit
) {
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
        Text(
            "El carnet digital de tu mascota",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onPrimary.copy(alpha = 0.85f)
        )
        Spacer(Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PetPrimaryButton(text = "Iniciar sesión", onClick = onLogin)
            PetOutlinedButton(text = "Crear cuenta", onClick = onSignUp, contentColor = colors.onPrimary)
            // Lets a visitor look around before deciding to register (HU-013).
            TextButton(
                onClick = onTryDemo,
                colors = ButtonDefaults.textButtonColors(contentColor = colors.onPrimary),
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) { Text("Probar demo", style = MaterialTheme.typography.labelLarge) }
            // Temporary entry point until the login screen exists (HU-010).
            TextButton(
                onClick = onForgotPassword,
                colors = ButtonDefaults.textButtonColors(contentColor = colors.onPrimary.copy(alpha = 0.85f)),
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
            ) { Text("¿Olvidaste tu contraseña?", style = MaterialTheme.typography.labelLarge) }
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    PetMateTheme {
        WelcomeScreen(onLogin = {}, onSignUp = {}, onTryDemo = {}, onForgotPassword = {})
    }
}
