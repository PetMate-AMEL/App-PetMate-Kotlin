package com.example.patitasid.data.repository

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.repository.AccountRepository

// Demo implementation: registrado@correo.com simulates an email that already has an account.
class AccountLocalRepository : AccountRepository {
    override suspend fun signUp(name: String, email: String, password: String): SealedResult<Session> =
        if (email.equals(TAKEN_EMAIL, ignoreCase = true)) {
            SealedResult.Failure(DomainError.EmailAlreadyUsed)
        } else {
            SealedResult.Success(
                Session(accessToken = "demo-acceso", refreshToken = "demo-renovacion", hasBusiness = false)
            )
        }

    companion object {
        const val TAKEN_EMAIL = "registrado@correo.com"
    }
}
