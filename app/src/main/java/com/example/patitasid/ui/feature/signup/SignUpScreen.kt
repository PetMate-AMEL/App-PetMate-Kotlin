package com.example.patitasid.ui.feature.signup

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.patitasid.ui.components.PetCard
import com.example.patitasid.ui.components.PetPasswordField
import com.example.patitasid.ui.components.PetPrimaryButton
import com.example.patitasid.ui.components.PetStatusMessage
import com.example.patitasid.ui.components.PetTextField
import com.example.patitasid.ui.components.PetTopBar
import com.example.patitasid.ui.theme.PetMateTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onAccountCreated: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // The session is already saved by this point, so the app can open "Mis mascotas" (RF-02).
    LaunchedEffect(state.isAccountCreated) {
        if (state.isAccountCreated) onAccountCreated()
    }
    SignUpContent(
        state = state,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmationChange = viewModel::onConfirmationChange,
        onSubmit = viewModel::submit
    )
}

@Composable
private fun SignUpContent(
    state: SignUpUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Scaffold(topBar = { PetTopBar(title = "Crear cuenta", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetCard {
                Text("Tus datos", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Con tu cuenta guardas el carnet de salud de tus mascotas y lo compartes cuando lo necesites.",
                    style = MaterialTheme.typography.bodyMedium
                )
                PetTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = "Nombre",
                    errorMessage = state.nameError,
                    enabled = !state.isSending,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                PetTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = "Correo electrónico",
                    errorMessage = state.emailError,
                    enabled = !state.isSending,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                )
                PetPasswordField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = "Contraseña",
                    errorMessage = state.passwordError,
                    enabled = !state.isSending
                )
                PetPasswordField(
                    value = state.confirmation,
                    onValueChange = onConfirmationChange,
                    label = "Confirmar contraseña",
                    errorMessage = state.confirmationError,
                    enabled = !state.isSending,
                    imeAction = ImeAction.Done
                )
                state.errorMessage?.let { PetStatusMessage(text = it, isError = true) }
                if (state.isSending) CircularProgressIndicator()
                PetPrimaryButton(text = "Crear cuenta", onClick = onSubmit, enabled = !state.isSending)
            }
        }
    }
}

@Preview
@Composable
private fun SignUpContentPreview() {
    PetMateTheme {
        SignUpContent(
            state = SignUpUiState(
                name = "Ana",
                email = "ana@correo.com",
                passwordError = SignUpViewModel.INVALID_PASSWORD_MESSAGE
            ),
            onBack = {},
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmationChange = {},
            onSubmit = {}
        )
    }
}
