package com.example.patitasid.ui.feature.signup

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.FakeSessionRepository
import com.example.patitasid.domain.usecase.SignUpUseCase
import com.example.patitasid.ui.common.UserMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
    private val accountRepository = FakeAccountRepository()
    private val sessionRepository = FakeSessionRepository()

    @BeforeEach
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModelWith(
        name: String = "Ana",
        email: String = "ana@correo.com",
        password: String = "mascota123",
        confirmation: String = "mascota123"
    ) = SignUpViewModel(SignUpUseCase(accountRepository, sessionRepository)).apply {
        onNameChange(name)
        onEmailChange(email)
        onPasswordChange(password)
        onConfirmationChange(confirmation)
    }

    @Test
    fun `valid data creates the account and saves the session`() {
        val viewModel = viewModelWith()

        viewModel.submit()

        val state = viewModel.uiState.value
        assertTrue(state.isAccountCreated)
        assertFalse(state.isSending)
        assertEquals(listOf("ana@correo.com"), accountRepository.signedUpEmails)
        assertNotNull(sessionRepository.session)
        assertEquals("acceso", sessionRepository.session?.accessToken)
    }

    @Test
    fun `passwords are cleared from the state once the account exists`() {
        val viewModel = viewModelWith()

        viewModel.submit()

        val state = viewModel.uiState.value
        assertEquals("", state.password)
        assertEquals("", state.confirmation)
    }

    @Test
    fun `an email already registered is shown next to the field`() {
        accountRepository.signUpResult = SealedResult.Failure(DomainError.EmailAlreadyUsed)
        val viewModel = viewModelWith(email = "registrado@correo.com")

        viewModel.submit()

        val state = viewModel.uiState.value
        assertEquals(UserMessages.EMAIL_ALREADY_USED, state.emailError)
        assertNull(state.errorMessage)
        assertFalse(state.isAccountCreated)
    }

    @Test
    fun `a password shorter than eight characters is rejected without calling the server`() {
        val viewModel = viewModelWith(password = "corta1", confirmation = "corta1")

        viewModel.submit()

        assertEquals(SignUpViewModel.INVALID_PASSWORD_MESSAGE, viewModel.uiState.value.passwordError)
        assertTrue(accountRepository.signedUpEmails.isEmpty())
        assertNull(sessionRepository.session)
    }

    @Test
    fun `a password without digits is rejected`() {
        val viewModel = viewModelWith(password = "solamenteletras", confirmation = "solamenteletras")

        viewModel.submit()

        assertEquals(SignUpViewModel.INVALID_PASSWORD_MESSAGE, viewModel.uiState.value.passwordError)
        assertTrue(accountRepository.signedUpEmails.isEmpty())
    }

    @Test
    fun `a confirmation that does not match is rejected`() {
        val viewModel = viewModelWith(confirmation = "mascota124")

        viewModel.submit()

        assertEquals(SignUpViewModel.PASSWORDS_DO_NOT_MATCH_MESSAGE, viewModel.uiState.value.confirmationError)
        assertTrue(accountRepository.signedUpEmails.isEmpty())
    }

    @Test
    fun `an invalid email is rejected without calling the server`() {
        val viewModel = viewModelWith(email = "ana.correo.com")

        viewModel.submit()

        assertEquals(SignUpViewModel.INVALID_EMAIL_MESSAGE, viewModel.uiState.value.emailError)
        assertTrue(accountRepository.signedUpEmails.isEmpty())
    }

    @Test
    fun `an empty name is rejected without calling the server`() {
        val viewModel = viewModelWith(name = "  ")

        viewModel.submit()

        assertEquals(SignUpViewModel.INVALID_NAME_MESSAGE, viewModel.uiState.value.nameError)
        assertTrue(accountRepository.signedUpEmails.isEmpty())
    }

    @Test
    fun `the name and the email are trimmed before reaching the server`() {
        val viewModel = viewModelWith(name = "  Ana  ", email = "  ana@correo.com  ")

        viewModel.submit()

        assertEquals(listOf("Ana"), accountRepository.signedUpNames)
        assertEquals(listOf("ana@correo.com"), accountRepository.signedUpEmails)
    }

    @Test
    fun `offline shows a clear message and keeps no session`() {
        accountRepository.signUpResult = SealedResult.Failure(DomainError.Network)
        val viewModel = viewModelWith()

        viewModel.submit()

        val state = viewModel.uiState.value
        assertEquals(UserMessages.NETWORK, state.errorMessage)
        assertFalse(state.isAccountCreated)
        assertNull(sessionRepository.session)
    }
}
