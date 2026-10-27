package com.example.patitasid.ui.feature.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.RequestPasswordResetUseCase
import com.example.patitasid.domain.validation.CredentialRules
import com.example.patitasid.ui.common.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isSending: Boolean = false,
    val sentMessage: String? = null,
    val errorMessage: String? = null
)

class ForgotPasswordViewModel(private val requestPasswordReset: RequestPasswordResetUseCase) : ViewModel() {
    private val states = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = states.asStateFlow()

    fun onEmailChange(email: String) {
        states.update { it.copy(email = email, emailError = null, errorMessage = null) }
    }

    fun send() {
        val state = states.value
        if (state.isSending) return
        if (!CredentialRules.isValidEmail(state.email)) {
            states.update { it.copy(emailError = INVALID_EMAIL_MESSAGE) }
            return
        }
        states.update { it.copy(isSending = true, sentMessage = null, errorMessage = null) }
        viewModelScope.launch {
            val result = requestPasswordReset(state.email)
            states.update {
                when (result) {
                    is SealedResult.Success -> it.copy(isSending = false, sentMessage = SENT_MESSAGE)
                    is SealedResult.Failure -> it.copy(isSending = false, errorMessage = result.error.toUserMessage())
                }
            }
        }
    }

    companion object {
        const val SENT_MESSAGE = "Si el correo existe, te enviamos un enlace."
        const val INVALID_EMAIL_MESSAGE = "Escribe un correo válido, por ejemplo nombre@correo.com."
    }
}
