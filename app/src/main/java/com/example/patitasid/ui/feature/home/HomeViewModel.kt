package com.example.patitasid.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.CheckServerHealthUseCase
import com.example.patitasid.util.toDisplayDate
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val message: String? = null,
    val lastCheck: String? = null
)

class HomeViewModel(private val checkServerHealth: CheckServerHealthUseCase) : ViewModel() {
    private val states = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = states.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        if (states.value.isLoading) return
        states.update { it.copy(isLoading = true, message = null) }
        viewModelScope.launch {
            val result = checkServerHealth()
            val today = LocalDate.now().toDisplayDate()
            states.update {
                when (result) {
                    is SealedResult.Success -> HomeUiState(isOnline = true, message = ONLINE_MESSAGE, lastCheck = today)
                    is SealedResult.Failure -> HomeUiState(isOnline = false, message = result.error.toUserMessage(), lastCheck = today)
                }
            }
        }
    }

    private fun DomainError.toUserMessage(): String = when (this) {
        DomainError.Network -> NETWORK_MESSAGE
        is DomainError.Server -> SERVER_MESSAGE
        DomainError.Unexpected -> SERVER_MESSAGE
    }

    companion object {
        const val ONLINE_MESSAGE = "Conectado al servidor de PetMate."
        const val NETWORK_MESSAGE = "No hay conexión a internet. Revisa tu red e inténtalo de nuevo."
        const val SERVER_MESSAGE = "El servidor no está disponible en este momento. Inténtalo más tarde."
    }
}
