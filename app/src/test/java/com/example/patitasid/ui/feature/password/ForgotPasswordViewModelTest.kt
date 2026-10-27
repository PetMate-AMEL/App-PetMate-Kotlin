package com.example.patitasid.ui.feature.password

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.RequestPasswordResetUseCase
import com.example.patitasid.ui.common.UserMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {
    private val repository = FakePasswordRepository()

    @BeforeEach
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModelWith(email: String) =
        ForgotPasswordViewModel(RequestPasswordResetUseCase(repository)).apply { onEmailChange(email) }

    @Test
    fun `valid email shows the neutral confirmation`() {
        val viewModel = viewModelWith(" ana@correo.com ")

        viewModel.send()

        val state = viewModel.uiState.value
        assertEquals("Si el correo existe, te enviamos un enlace.", state.sentMessage)
        assertFalse(state.isSending)
        assertEquals(listOf("ana@correo.com"), repository.requestedEmails)
    }

    @Test
    fun `unknown email shows the same confirmation`() {
        repository.requestResult = SealedResult.Failure(DomainError.Server(404))
        val viewModel = viewModelWith("nadie@correo.com")

        viewModel.send()

        assertEquals(ForgotPasswordViewModel.SENT_MESSAGE, viewModel.uiState.value.sentMessage)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `invalid email is rejected without calling the server`() {
        val viewModel = viewModelWith("ana.correo.com")

        viewModel.send()

        assertEquals(ForgotPasswordViewModel.INVALID_EMAIL_MESSAGE, viewModel.uiState.value.emailError)
        assertTrue(repository.requestedEmails.isEmpty())
    }

    @Test
    fun `offline shows a clear message`() {
        repository.requestResult = SealedResult.Failure(DomainError.Network)
        val viewModel = viewModelWith("ana@correo.com")

        viewModel.send()

        assertEquals(UserMessages.NETWORK, viewModel.uiState.value.errorMessage)
        assertNull(viewModel.uiState.value.sentMessage)
    }
}
