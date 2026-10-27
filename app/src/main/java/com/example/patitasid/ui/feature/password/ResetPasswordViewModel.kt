package com.example.patitasid.ui.feature.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.ResetPasswordUseCase
import com.example.patitasid.domain.usecase.ValidateResetLinkUseCase
import com.example.patitasid.domain.validation.CredentialRules
import com.example.patitasid.ui.common.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ResetLinkStatus { Checking, CheckFailed, Valid, Expired, Saved }

data class ResetPasswordUiState(
    val status: ResetLinkStatus = ResetLinkStatus.Checking,
    val password: String = "",
    val confirmation: String = "",
    val passwordError: String? = null,
    val confirmationError: String? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

class ResetPasswordViewModel(
    private val token: String,
    private val validateResetLink: ValidateResetLinkUseCase,
    private val resetPassword: ResetPasswordUseCase
) : ViewModel() {
    private val states = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = states.asStateFlow()

    init {
        checkLink()
    }

    fun checkLink() {
        states.update { it.copy(status = ResetLinkStatus.Checking, errorMessage = null) }
        viewModelScope.launch {
            val result = validateResetLink(token)
            states.update {
                when (result) {
                    is SealedResult.Success -> it.copy(status = ResetLinkStatus.Valid)
                    is SealedResult.Failure if result.error == DomainError.ExpiredLink -> it.copy(status = ResetLinkStatus.Expired)
                    is SealedResult.Failure -> it.copy(status = ResetLinkStatus.CheckFailed, errorMessage = result.error.toUserMessage())
                }
            }
        }
    }

    fun onPasswordChange(password: String) {
        states.update { it.copy(password = password, passwordError = null, errorMessage = null) }
    }

    fun onConfirmationChange(confirmation: String) {
        states.update { it.copy(confirmation = confirmation, confirmationError = null, errorMessage = null) }
    }

    fun save() {
        val state = states.value
        if (state.isSaving || state.status != ResetLinkStatus.Valid) return
        val passwordError = if (CredentialRules.isValidPassword(state.password)) null else WEAK_PASSWORD_MESSAGE
        val confirmationError = if (state.confirmation == state.password) null else MISMATCH_MESSAGE
        if (passwordError != null || confirmationError != null) {
            states.update { it.copy(passwordError = passwordError, confirmationError = confirmationError) }
            return
        }
        states.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            val result = resetPassword(token, state.password)
            states.update {
                when (result) {
                    // Drop the password from memory once it is saved.
                    is SealedResult.Success -> ResetPasswordUiState(status = ResetLinkStatus.Saved)
                    is SealedResult.Failure if result.error == DomainError.ExpiredLink -> ResetPasswordUiState(status = ResetLinkStatus.Expired)
                    is SealedResult.Failure -> it.copy(isSaving = false, errorMessage = result.error.toUserMessage())
                }
            }
        }
    }

    companion object {
        val WEAK_PASSWORD_MESSAGE = "Usa al menos ${CredentialRules.PASSWORD_MIN_LENGTH} caracteres, con letras y números."
        const val MISMATCH_MESSAGE = "Las contraseñas no coinciden."
    }
}
