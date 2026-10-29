package com.example.patitasid.data.repository

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.data.remote.ForgotPasswordRequest
import com.example.patitasid.data.remote.PasswordApiService
import com.example.patitasid.data.remote.ResetLinkRequest
import com.example.patitasid.data.remote.ResetPasswordRequest
import com.example.patitasid.data.remote.safeApiCallNoBody
import com.example.patitasid.domain.repository.PasswordRepository

// 404: unknown token; 410: expired or already used.
private val EXPIRED_LINK_CODES = setOf(404, 410)

class PasswordRemoteRepository(private val api: PasswordApiService) : PasswordRepository {
    override suspend fun requestReset(email: String): SealedResult<Unit> =
        safeApiCallNoBody { api.requestReset(ForgotPasswordRequest(correo = email)) }

    override suspend fun validateResetLink(token: String): SealedResult<Unit> =
        safeApiCallNoBody { api.validateResetLink(ResetLinkRequest(token = token)) }.withExpiredLink()

    override suspend fun resetPassword(token: String, newPassword: String): SealedResult<Unit> =
        safeApiCallNoBody { api.resetPassword(ResetPasswordRequest(token = token, nuevaContrasena = newPassword)) }.withExpiredLink()

    private fun SealedResult<Unit>.withExpiredLink(): SealedResult<Unit> {
        val error = (this as? SealedResult.Failure)?.error
        return if (error is DomainError.Server && error.code in EXPIRED_LINK_CODES) SealedResult.Failure(DomainError.ExpiredLink) else this
    }
}
