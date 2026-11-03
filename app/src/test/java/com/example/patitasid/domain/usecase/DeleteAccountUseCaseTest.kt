package com.example.patitasid.domain.usecase

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.model.Session
import com.example.patitasid.ui.feature.signup.FakeAccountRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DeleteAccountUseCaseTest {
    private val session = Session(accessToken = "acceso", refreshToken = "renovacion", hasBusiness = false)
    private val accounts = FakeAccountRepository()
    private val sessions = FakeSessionRepository(session)
    private val deleteAccount = DeleteAccountUseCase(accounts, sessions)

    @Test
    fun `deleted account clears the saved session`() = runTest {
        val result = deleteAccount()

        assertEquals(SealedResult.Success(Unit), result)
        assertEquals(listOf("acceso"), accounts.deletedTokens)
        assertNull(sessions.session)
    }

    @Test
    fun `failed deletion keeps the session so it can be retried`() = runTest {
        accounts.deleteResult = SealedResult.Failure(DomainError.Network)

        val result = deleteAccount()

        assertEquals(SealedResult.Failure(DomainError.Network), result)
        assertNotNull(sessions.session)
        assertEquals(0, sessions.clearCount)
    }

    @Test
    fun `without a session there is nothing to delete on the server`() = runTest {
        sessions.session = null

        val result = deleteAccount()

        assertEquals(SealedResult.Success(Unit), result)
        assertTrue(accounts.deletedTokens.isEmpty())
    }
}
