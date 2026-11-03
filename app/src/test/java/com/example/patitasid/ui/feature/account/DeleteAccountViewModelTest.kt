package com.example.patitasid.ui.feature.account

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.usecase.DeleteAccountUseCase
import com.example.patitasid.domain.usecase.FakeSessionRepository
import com.example.patitasid.ui.common.UserMessages
import com.example.patitasid.ui.feature.signup.FakeAccountRepository
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
class DeleteAccountViewModelTest {
    private val accounts = FakeAccountRepository()
    private val sessions = FakeSessionRepository(Session(accessToken = "acceso", refreshToken = "renovacion", hasBusiness = false))
    private val viewModel by lazy { DeleteAccountViewModel(DeleteAccountUseCase(accounts, sessions)) }

    @BeforeEach
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `first tap only asks for confirmation`() {
        viewModel.requestDelete()

        assertTrue(viewModel.uiState.value.isConfirming)
        assertTrue(accounts.deletedTokens.isEmpty())
    }

    @Test
    fun `cancel closes the confirmation without deleting`() {
        viewModel.requestDelete()
        viewModel.cancel()

        assertFalse(viewModel.uiState.value.isConfirming)
        assertTrue(accounts.deletedTokens.isEmpty())
    }

    @Test
    fun `confirming deletes the account`() {
        viewModel.requestDelete()
        viewModel.confirmDelete()

        val state = viewModel.uiState.value
        assertTrue(state.isDeleted)
        assertFalse(state.isDeleting)
        assertEquals(listOf("acceso"), accounts.deletedTokens)
    }

    @Test
    fun `without internet shows the network message`() {
        accounts.deleteResult = SealedResult.Failure(DomainError.Network)

        viewModel.requestDelete()
        viewModel.confirmDelete()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleted)
        assertEquals(UserMessages.NETWORK, state.errorMessage)
    }
}
