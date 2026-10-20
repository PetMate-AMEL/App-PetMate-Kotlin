package com.example.patitasid.ui.feature.password

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.usecase.ResetPasswordUseCase
import com.example.patitasid.domain.usecase.ValidateResetLinkUseCase
import com.example.patitasid.ui.common.UserMessages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResetPasswordViewModelTest {
    private val repository = FakePasswordRepository()

    @BeforeEach
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel() = ResetPasswordViewModel(
        token = "abc123",
        validateResetLink = ValidateResetLinkUseCase(repository),
        resetPassword = ResetPasswordUseCase(repository)
    )

    private fun ResetPasswordViewModel.fill(password: String, confirmation: String = password) = apply {
        onPasswordChange(password)
        onConfirmationChange(confirmation)
    }

    @Test
    fun `valid password typed twice is saved`() {
        val viewModel = viewModel().fill("Firulais2026")

        viewModel.save()

        assertEquals(ResetLinkStatus.Saved, viewModel.uiState.value.status)
        assertEquals("", viewModel.uiState.value.password)
        assertEquals(listOf("Firulais2026"), repository.savedPasswords)
    }

    @Test
    fun `expired link is shown when opened`() {
        repository.validateResult = SealedResult.Failure(DomainError.ExpiredLink)

        assertEquals(ResetLinkStatus.Expired, viewModel().uiState.value.status)
    }

    @Test
    fun `link that expires while typing is shown as expired`() {
        repository.resetResult = SealedResult.Failure(DomainError.ExpiredLink)
        val viewModel = viewModel().fill("Firulais2026")

        viewModel.save()

        assertEquals(ResetLinkStatus.Expired, viewModel.uiState.value.status)
    }

    @Test
    fun `different confirmation is rejected`() {
        val viewModel = viewModel().fill("Firulais2026", "Firulais2025")

        viewModel.save()

        assertEquals(ResetPasswordViewModel.MISMATCH_MESSAGE, viewModel.uiState.value.confirmationError)
        assertTrue(repository.savedPasswords.isEmpty())
    }

    @Test
    fun `weak password is rejected`() {
        val viewModel = viewModel().fill("perros")

        viewModel.save()

        assertEquals(ResetPasswordViewModel.WEAK_PASSWORD_MESSAGE, viewModel.uiState.value.passwordError)
        assertTrue(repository.savedPasswords.isEmpty())
    }

    @Test
    fun `offline while checking the link allows retrying`() {
        repository.validateResult = SealedResult.Failure(DomainError.Network)
        val viewModel = viewModel()
        assertEquals(ResetLinkStatus.CheckFailed, viewModel.uiState.value.status)
        assertEquals(UserMessages.NETWORK, viewModel.uiState.value.errorMessage)

        repository.validateResult = SealedResult.Success(Unit)
        viewModel.checkLink()

        assertEquals(ResetLinkStatus.Valid, viewModel.uiState.value.status)
    }
}
