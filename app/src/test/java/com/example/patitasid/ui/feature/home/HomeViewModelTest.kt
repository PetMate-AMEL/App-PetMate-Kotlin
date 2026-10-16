package com.example.patitasid.ui.feature.home

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.data.repository.HealthLocalRepository
import com.example.patitasid.domain.repository.HealthRepository
import com.example.patitasid.domain.usecase.CheckServerHealthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @BeforeEach
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `local repository works without changing the screen`() {
        val state = HomeViewModel(CheckServerHealthUseCase(HealthLocalRepository())).uiState.value

        assertTrue(state.isOnline)
        assertEquals(HomeViewModel.ONLINE_MESSAGE, state.message)
        assertTrue(state.lastCheck!!.matches(Regex("""\d{2}/\d{2}/\d{4}""")))
    }

    @Test
    fun `offline shows a clear message`() {
        val offline = object : HealthRepository {
            override suspend fun checkHealth() = SealedResult.Failure(DomainError.Network)
        }

        val state = HomeViewModel(CheckServerHealthUseCase(offline)).uiState.value

        assertFalse(state.isOnline)
        assertFalse(state.isLoading)
        assertEquals(HomeViewModel.NETWORK_MESSAGE, state.message)
    }
}
