package com.example.patitasid.data.remote

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Contract pending confirmation with the API team (see specs/HU-011-forgot-password/spec.md).
interface PasswordApiService {
    @POST("api/auth/olvide-contrasena")
    suspend fun requestReset(@Body body: ForgotPasswordRequest): Response<Unit>

    // Token goes in the body, not the URL, so it does not end up in server logs.
    @POST("api/auth/restablecer-contrasena/validar")
    suspend fun validateResetLink(@Body body: ResetLinkRequest): Response<Unit>

    @POST("api/auth/restablecer-contrasena")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<Unit>
}

@Serializable
data class ForgotPasswordRequest(val correo: String)

@Serializable
data class ResetLinkRequest(val token: String)

@Serializable
data class ResetPasswordRequest(val token: String, val nuevaContrasena: String)
