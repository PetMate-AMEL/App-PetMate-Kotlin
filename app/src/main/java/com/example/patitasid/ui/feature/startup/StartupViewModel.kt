package com.example.patitasid.ui.feature.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patitasid.domain.model.StartDestination
import com.example.patitasid.domain.usecase.GetStartDestinationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// null while the saved session is being read: the app shows a loading screen instead of the welcome one.
data class StartupUiState(val destination: StartDestination? = null)

class StartupViewModel(getStartDestination: GetStartDestinationUseCase) : ViewModel() {
    private val states = MutableStateFlow(StartupUiState())
    val uiState: StateFlow<StartupUiState> = states.asStateFlow()

    init {
        viewModelScope.launch {
            states.value = StartupUiState(destination = getStartDestination())
        }
    }
}
