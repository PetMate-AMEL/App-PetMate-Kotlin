package com.example.patitasid.ui.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.patitasid.ui.components.PetCard
import com.example.patitasid.ui.components.PetOutlinedButton
import com.example.patitasid.ui.components.PetStatusMessage
import com.example.patitasid.ui.components.PetTopBar
import com.example.patitasid.ui.theme.PetMateTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeleteAccountScreen(
    onBack: () -> Unit,
    onAccountDeleted: () -> Unit,
    viewModel: DeleteAccountViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onAccountDeleted()
    }
    DeleteAccountContent(
        state = state,
        onBack = onBack,
        onRequestDelete = viewModel::requestDelete,
        onConfirm = viewModel::confirmDelete,
        onCancel = viewModel::cancel
    )
}

@Composable
private fun DeleteAccountContent(
    state: DeleteAccountUiState,
    onBack: () -> Unit,
    onRequestDelete: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(topBar = { PetTopBar(title = "Eliminar mi cuenta", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetCard {
                Text("¿Quieres eliminar tu cuenta?", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Se borrarán de forma permanente tu cuenta, tus datos personales y la información de tus mascotas. " +
                        "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodyMedium
                )
                state.errorMessage?.let { PetStatusMessage(text = it, isError = true) }
                if (state.isDeleting) CircularProgressIndicator()
                PetOutlinedButton(
                    text = "Eliminar mi cuenta",
                    onClick = onRequestDelete,
                    contentColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    if (state.isConfirming) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text("Eliminar cuenta") },
            text = { Text("Tu cuenta y todos tus datos se borrarán para siempre. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(onClick = onConfirm) { Text("Sí, eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = onCancel) { Text("Cancelar") } }
        )
    }
}

@Preview
@Composable
private fun DeleteAccountPreview() {
    PetMateTheme {
        DeleteAccountContent(state = DeleteAccountUiState(), onBack = {}, onRequestDelete = {}, onConfirm = {}, onCancel = {})
    }
}
