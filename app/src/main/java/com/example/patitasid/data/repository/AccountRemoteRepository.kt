package com.example.patitasid.data.repository

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.data.remote.AccountApiService
import com.example.patitasid.data.remote.SignUpRequest
import com.example.patitasid.data.remote.safeApiCall
import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.repository.AccountRepository
import java.net.HttpURLConnection

class AccountRemoteRepository(private val api: AccountApiService) : AccountRepository {
    override suspend fun signUp(name: String, email: String, password: String): SealedResult<Session> {
        val result = safeApiCall {
            api.signUp(SignUpRequest(nombre = name, correo = email, contrasena = password))
        }
        return when (result) {
            is SealedResult.Success -> SealedResult.Success(
                Session(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    hasBusiness = result.data.tieneNegocio
                )
            )
            is SealedResult.Failure -> {
                val error = result.error
                // 409: the email already has an account, shown next to the field instead of as a general error.
                if (error is DomainError.Server && error.code == HttpURLConnection.HTTP_CONFLICT) {
                    SealedResult.Failure(DomainError.EmailAlreadyUsed)
                } else {
                    result
                }
            }
        }
    }
}
