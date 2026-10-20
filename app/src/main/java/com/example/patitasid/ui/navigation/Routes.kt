package com.example.patitasid.ui.navigation

import kotlinx.serialization.Serializable

// Link sent by email: petmate://restablecer-contrasena/{token}
const val RESET_PASSWORD_DEEP_LINK = "petmate://restablecer-contrasena"

@Serializable
object WelcomeRoute

@Serializable
object HomeRoute

@Serializable
object ForgotPasswordRoute

@Serializable
data class ResetPasswordRoute(val token: String)
