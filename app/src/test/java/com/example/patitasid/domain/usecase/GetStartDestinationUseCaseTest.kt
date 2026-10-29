package com.example.patitasid.domain.usecase

import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.model.StartDestination
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetStartDestinationUseCaseTest {
    private val sessionRepository = FakeSessionRepository()
    private val getStartDestination = GetStartDestinationUseCase(sessionRepository)

    @Test
    fun `without a session the app opens the welcome screen`() = runTest {
        sessionRepository.session = null

        assertEquals(StartDestination.Welcome, getStartDestination())
    }

    @Test
    fun `a session without a business opens my pets`() = runTest {
        sessionRepository.session = Session("acceso", "renovacion", hasBusiness = false)

        assertEquals(StartDestination.Pets, getStartDestination())
    }

    @Test
    fun `a session with a business opens the business panel`() = runTest {
        sessionRepository.session = Session("acceso", "renovacion", hasBusiness = true)

        assertEquals(StartDestination.BusinessPanel, getStartDestination())
    }
}
