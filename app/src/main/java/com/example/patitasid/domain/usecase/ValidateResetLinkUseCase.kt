package com.example.patitasid.domain.usecase

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.PasswordRepository

class ValidateResetLinkUseCase(private val passwordRepository: PasswordRepository) {
    suspend operator fun invoke(token: String): SealedResult<Unit> = passwordRepository.validateResetLink(token)
}
