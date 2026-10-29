package com.example.patitasid.ui.feature.startup

import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.model.StartDestination
import com.example.patitasid.domain.usecase.FakeSessionRepository
import com.example.patitasid.domain.usecase.GetStartDestinationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartupViewModelTest {
    private val sessionRepository = FakeSessionRepository()

    @BeforeEach
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel() = StartupViewModel(GetStartDestinationUseCase(sessionRepository))

    @Test
    fun `without a session it sends the visitor to the welcome screen`() {
        sessionRepository.session = null

        assertEquals(StartDestination.Welcome, viewModel().uiState.value.destination)
    }

    @Test
    fun `with a saved session it skips the welcome screen`() {
        sessionRepository.session = Session("acceso", "renovacion", hasBusiness = false)

        assertEquals(StartDestination.Pets, viewModel().uiState.value.destination)
    }

    @Test
    fun `a session with a business opens the panel`() {
        sessionRepository.session = Session("acceso", "renovacion", hasBusiness = true)

        assertEquals(StartDestination.BusinessPanel, viewModel().uiState.value.destination)
    }
}
