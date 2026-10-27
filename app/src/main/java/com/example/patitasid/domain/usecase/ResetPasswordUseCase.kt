package com.example.patitasid.domain.usecase

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.PasswordRepository

class ResetPasswordUseCase(private val passwordRepository: PasswordRepository) {
    suspend operator fun invoke(token: String, newPassword: String): SealedResult<Unit> =
        passwordRepository.resetPassword(token, newPassword)
}
