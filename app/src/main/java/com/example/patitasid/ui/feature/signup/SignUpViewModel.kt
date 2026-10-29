package com.example.patitasid.ui.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.SignUpUseCase
import com.example.patitasid.domain.validation.CredentialRules
import com.example.patitasid.ui.common.UserMessages
import com.example.patitasid.ui.common.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmation: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmationError: String? = null,
    val isSending: Boolean = false,
    val errorMessage: String? = null,
    val isAccountCreated: Boolean = false
)

class SignUpViewModel(private val signUp: SignUpUseCase) : ViewModel() {
    private val states = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = states.asStateFlow()

    fun onNameChange(name: String) {
        states.update { it.copy(name = name, nameError = null, errorMessage = null) }
    }

    fun onEmailChange(email: String) {
        states.update { it.copy(email = email, emailError = null, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        states.update { it.copy(password = password, passwordError = null, errorMessage = null) }
    }

    fun onConfirmationChange(confirmation: String) {
        states.update { it.copy(confirmation = confirmation, confirmationError = null, errorMessage = null) }
    }

    fun submit() {
        val state = states.value
        if (state.isSending) return
        // Everything is checked on screen before spending a request (RF-02).
        val validated = state.copy(
            nameError = if (CredentialRules.isValidName(state.name)) null else INVALID_NAME_MESSAGE,
            emailError = if (CredentialRules.isValidEmail(state.email)) null else INVALID_EMAIL_MESSAGE,
            passwordError = if (CredentialRules.isValidPassword(state.password)) null else INVALID_PASSWORD_MESSAGE,
            confirmationError = if (state.password == state.confirmation) null else PASSWORDS_DO_NOT_MATCH_MESSAGE
        )
        val hasErrors = listOf(
            validated.nameError,
            validated.emailError,
            validated.passwordError,
            validated.confirmationError
        ).any { it != null }
        if (hasErrors) {
            states.value = validated
            return
        }
        states.value = validated.copy(isSending = true, errorMessage = null)
        viewModelScope.launch {
            val result = signUp(name = state.name, email = state.email, password = state.password)
            states.update {
                when (result) {
                    // Passwords are dropped from the state as soon as the account exists.
                    is SealedResult.Success -> it.copy(
                        password = "",
                        confirmation = "",
                        isSending = false,
                        isAccountCreated = true
                    )
                    is SealedResult.Failure -> when (result.error) {
                        DomainError.EmailAlreadyUsed -> it.copy(
                            isSending = false,
                            emailError = UserMessages.EMAIL_ALREADY_USED
                        )
                        else -> it.copy(isSending = false, errorMessage = result.error.toUserMessage())
                    }
                }
            }
        }
    }

    companion object {
        const val INVALID_NAME_MESSAGE = "Escribe tu nombre."
        const val INVALID_EMAIL_MESSAGE = "Escribe un correo válido, por ejemplo nombre@correo.com."
        const val INVALID_PASSWORD_MESSAGE = "Usa al menos 8 caracteres, con letras y números."
        const val PASSWORDS_DO_NOT_MATCH_MESSAGE = "Las contraseñas no son iguales."
    }
}
