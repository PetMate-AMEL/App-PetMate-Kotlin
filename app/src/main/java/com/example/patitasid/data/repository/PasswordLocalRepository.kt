package com.example.patitasid.data.repository

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.PasswordRepository

// Demo implementation: petmate://restablecer-contrasena/expirado simulates an expired link.
class PasswordLocalRepository : PasswordRepository {
    override suspend fun requestReset(email: String): SealedResult<Unit> = SealedResult.Success(Unit)

    override suspend fun validateResetLink(token: String): SealedResult<Unit> = checkToken(token)

    override suspend fun resetPassword(token: String, newPassword: String): SealedResult<Unit> = checkToken(token)

    private fun checkToken(token: String): SealedResult<Unit> =
        if (token == EXPIRED_TOKEN) SealedResult.Failure(DomainError.ExpiredLink) else SealedResult.Success(Unit)

    companion object {
        const val EXPIRED_TOKEN = "expirado"
    }
}
