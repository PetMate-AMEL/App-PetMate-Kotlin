package com.example.patitasid.domain.usecase

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.PasswordRepository
import java.net.HttpURLConnection

class RequestPasswordResetUseCase(private val passwordRepository: PasswordRepository) {
    suspend operator fun invoke(email: String): SealedResult<Unit> {
        val result = passwordRepository.requestReset(email.trim())
        // Never reveal whether an email is registered: "not found" looks the same as a success.
        val failure = (result as? SealedResult.Failure)?.error
        return if (failure is DomainError.Server && failure.code == HttpURLConnection.HTTP_NOT_FOUND) SealedResult.Success(Unit) else result
    }
}
