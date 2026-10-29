package com.example.patitasid.data.remote

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Contract pending confirmation with the API team (see specs/HU-009-crear-cuenta/spec.md).
interface AccountApiService {
    @POST("api/auth/registro")
    suspend fun signUp(@Body body: SignUpRequest): Response<SessionResponse>
}

@Serializable
data class SignUpRequest(val nombre: String, val correo: String, val contrasena: String)

@Serializable
data class SessionResponse(val accessToken: String, val refreshToken: String, val tieneNegocio: Boolean = false)
