package com.example.patitasid.ui.feature.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.patitasid.ui.components.PetCard
import com.example.patitasid.ui.components.PetPrimaryButton
import com.example.patitasid.ui.components.PetStatusMessage
import com.example.patitasid.ui.components.PetTextField
import com.example.patitasid.ui.components.PetTopBar
import com.example.patitasid.ui.theme.PetMateTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(onBack: () -> Unit, viewModel: ForgotPasswordViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ForgotPasswordContent(state = state, onBack = onBack, onEmailChange = viewModel::onEmailChange, onSend = viewModel::send)
}

@Composable
private fun ForgotPasswordContent(
    state: ForgotPasswordUiState,
    onBack: () -> Unit,
    onEmailChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Scaffold(topBar = { PetTopBar(title = "Olvidé mi contraseña", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetCard {
                Text("Recupera tu acceso", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Escribe el correo con el que te registraste y te enviaremos un enlace para crear una contraseña nueva.",
                    style = MaterialTheme.typography.bodyMedium
                )
                PetTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = "Correo electrónico",
                    errorMessage = state.emailError,
                    enabled = !state.isSending,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done)
                )
                state.sentMessage?.let { PetStatusMessage(text = it, isError = false) }
                state.errorMessage?.let { PetStatusMessage(text = it, isError = true) }
                if (state.isSending) CircularProgressIndicator()
                PetPrimaryButton(
                    text = if (state.sentMessage == null) "Enviar enlace" else "Reenviar enlace",
                    onClick = onSend,
                    enabled = !state.isSending
                )
            }
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordSentPreview() {
    PetMateTheme {
        ForgotPasswordContent(
            state = ForgotPasswordUiState(email = "ana@correo.com", sentMessage = ForgotPasswordViewModel.SENT_MESSAGE),
            onBack = {},
            onEmailChange = {},
            onSend = {}
        )
    }
}
