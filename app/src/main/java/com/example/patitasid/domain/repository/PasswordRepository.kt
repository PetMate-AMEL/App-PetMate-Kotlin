package com.example.patitasid.domain.repository

import com.example.patitasid.common.SealedResult

interface PasswordRepository {
    suspend fun requestReset(email: String): SealedResult<Unit>

    // Failure(DomainError.ExpiredLink) when the token expired or was already used.
    suspend fun validateResetLink(token: String): SealedResult<Unit>

    suspend fun resetPassword(token: String, newPassword: String): SealedResult<Unit>
}
