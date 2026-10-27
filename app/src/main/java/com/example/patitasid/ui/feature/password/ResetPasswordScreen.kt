package com.example.patitasid.ui.feature.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.patitasid.ui.components.PetCard
import com.example.patitasid.ui.components.PetPasswordField
import com.example.patitasid.ui.components.PetPrimaryButton
import com.example.patitasid.ui.components.PetStatusMessage
import com.example.patitasid.ui.components.PetTopBar
import com.example.patitasid.ui.theme.PetMateTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResetPasswordScreen(
    token: String,
    onBack: () -> Unit,
    onRequestNewLink: () -> Unit,
    onGoToLogin: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel(key = token) { parametersOf(token) }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ResetPasswordContent(
        state = state,
        onBack = onBack,
        onRetry = viewModel::checkLink,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmationChange = viewModel::onConfirmationChange,
        onSave = viewModel::save,
        onRequestNewLink = onRequestNewLink,
        onGoToLogin = onGoToLogin
    )
}

@Composable
private fun ResetPasswordContent(
    state: ResetPasswordUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onSave: () -> Unit,
    onRequestNewLink: () -> Unit,
    onGoToLogin: () -> Unit
) {
    Scaffold(topBar = { PetTopBar(title = "Nueva contraseña", onBack = onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PetCard {
                when (state.status) {
                    ResetLinkStatus.Checking -> {
                        Text("Revisando tu enlace…", style = MaterialTheme.typography.titleMedium)
                        CircularProgressIndicator()
                    }
                    ResetLinkStatus.CheckFailed -> {
                        Text("No pudimos revisar tu enlace", style = MaterialTheme.typography.titleMedium)
                        state.errorMessage?.let { PetStatusMessage(text = it, isError = true) }
                        PetPrimaryButton(text = "Reintentar", onClick = onRetry)
                    }
                    ResetLinkStatus.Valid -> {
                        Text("Crea tu contraseña nueva", style = MaterialTheme.typography.titleMedium)
                        Text(ResetPasswordViewModel.WEAK_PASSWORD_MESSAGE, style = MaterialTheme.typography.bodyMedium)
                        PetPasswordField(
                            value = state.password,
                            onValueChange = onPasswordChange,
                            label = "Contraseña nueva",
                            errorMessage = state.passwordError,
                            enabled = !state.isSaving
                        )
                        PetPasswordField(
                            value = state.confirmation,
                            onValueChange = onConfirmationChange,
                            label = "Confirma tu contraseña",
                            errorMessage = state.confirmationError,
                            enabled = !state.isSaving,
                            imeAction = ImeAction.Done
                        )
                        state.errorMessage?.let { PetStatusMessage(text = it, isError = true) }
                        if (state.isSaving) CircularProgressIndicator()
                        PetPrimaryButton(text = "Guardar contraseña", onClick = onSave, enabled = !state.isSaving)
                    }
                    ResetLinkStatus.Expired -> {
                        Text("Este enlace ya no es válido", style = MaterialTheme.typography.titleMedium)
                        PetStatusMessage(
                            text = "El enlace expiró o ya se usó. Pide uno nuevo para restablecer tu contraseña.",
                            isError = true
                        )
                        PetPrimaryButton(text = "Pedir otro enlace", onClick = onRequestNewLink)
                    }
                    ResetLinkStatus.Saved -> {
                        Text("Tu contraseña se guardó", style = MaterialTheme.typography.titleMedium)
                        PetStatusMessage(text = "Ya puedes iniciar sesión con tu contraseña nueva.", isError = false)
                        PetPrimaryButton(text = "Iniciar sesión", onClick = onGoToLogin)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ResetPasswordFormPreview() {
    PetMateTheme {
        ResetPasswordContent(
            state = ResetPasswordUiState(status = ResetLinkStatus.Valid, confirmationError = ResetPasswordViewModel.MISMATCH_MESSAGE),
            onBack = {}, onRetry = {}, onPasswordChange = {}, onConfirmationChange = {}, onSave = {}, onRequestNewLink = {}, onGoToLogin = {}
        )
    }
}

@Preview
@Composable
private fun ResetPasswordExpiredPreview() {
    PetMateTheme {
        ResetPasswordContent(
            state = ResetPasswordUiState(status = ResetLinkStatus.Expired),
            onBack = {}, onRetry = {}, onPasswordChange = {}, onConfirmationChange = {}, onSave = {}, onRequestNewLink = {}, onGoToLogin = {}
        )
    }
}
