package com.example.patitasid.ui.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.DeleteAccountUseCase
import com.example.patitasid.ui.common.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeleteAccountUiState(
    val isConfirming: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null
)

class DeleteAccountViewModel(private val deleteAccount: DeleteAccountUseCase) : ViewModel() {
    private val states = MutableStateFlow(DeleteAccountUiState())
    val uiState: StateFlow<DeleteAccountUiState> = states.asStateFlow()

    // First tap only asks for confirmation; nothing is deleted yet.
    fun requestDelete() {
        if (states.value.isDeleting) return
        states.update { it.copy(isConfirming = true, errorMessage = null) }
    }

    fun cancel() {
        states.update { it.copy(isConfirming = false) }
    }

    fun confirmDelete() {
        if (states.value.isDeleting) return
        states.update { it.copy(isConfirming = false, isDeleting = true, errorMessage = null) }
        viewModelScope.launch {
            val result = deleteAccount()
            states.update {
                when (result) {
                    is SealedResult.Success -> it.copy(isDeleting = false, isDeleted = true)
                    is SealedResult.Failure -> it.copy(isDeleting = false, errorMessage = result.error.toUserMessage())
                }
            }
        }
    }
}
