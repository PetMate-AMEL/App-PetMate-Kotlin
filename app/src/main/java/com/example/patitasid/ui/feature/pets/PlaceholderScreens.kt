package com.example.patitasid.ui.feature.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.patitasid.ui.components.PetCard
import com.example.patitasid.ui.components.PetOutlinedButton
import com.example.patitasid.ui.components.PetTopBar
import com.example.patitasid.ui.theme.PetMateTheme

// Placeholders so the start destination of HU-008 can be reached and tested.
// "Mis mascotas" is built in HU-018 and the affiliate panel in HU-044.
@Composable
fun PetsScreen(onBack: () -> Unit, onDeleteAccount: () -> Unit) {
    PlaceholderContent(
        title = "Mis mascotas",
        message = "Aquí van las tarjetas de tus mascotas con su foto y lo más próximo resaltado (HU-018).",
        onBack = onBack,
        onDeleteAccount = onDeleteAccount
    )
}

@Composable
fun BusinessPanelScreen(onBack: () -> Unit, onDeleteAccount: () -> Unit) {
    PlaceholderContent(
        title = "Panel del negocio",
        message = "Aquí van la portada del negocio y las alertas de seguimiento (HU-044).",
        onBack = onBack,
        onDeleteAccount = onDeleteAccount
    )
}

@Composable
fun DemoScreen(onBack: () -> Unit) {
    PlaceholderContent(
        title = "Modo demo",
        message = "Aquí va la probadita con datos de ejemplo y secciones bloqueadas (HU-013).",
        onBack = onBack
    )
}

@Composable
private fun PlaceholderContent(title: String, message: String, onBack: () -> Unit, onDeleteAccount: (() -> Unit)? = null) {
    Scaffold(topBar = { PetTopBar(title = title, onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetCard {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(message, style = MaterialTheme.typography.bodyMedium)
            }
            // Temporary entry point until the account/settings screen exists (Play account deletion policy).
            onDeleteAccount?.let {
                PetOutlinedButton(text = "Eliminar mi cuenta", onClick = it, contentColor = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview
@Composable
private fun PetsScreenPreview() {
    PetMateTheme { PetsScreen(onBack = {}, onDeleteAccount = {}) }
}
