package com.example.patitasid.ui.navigation

import kotlinx.serialization.Serializable

// Link sent by email: petmate://restablecer-contrasena/{token}
const val RESET_PASSWORD_DEEP_LINK = "petmate://restablecer-contrasena"

// Decides between welcome, pets and business panel while the saved session is read (HU-008).
@Serializable
object StartupRoute

@Serializable
object WelcomeRoute

@Serializable
object HomeRoute

@Serializable
object SignUpRoute

// Placeholder until HU-010 builds the login screen.
@Serializable
object LoginRoute

@Serializable
object DemoRoute

@Serializable
object PetsRoute

@Serializable
object BusinessPanelRoute

@Serializable
object ForgotPasswordRoute

@Serializable
data class ResetPasswordRoute(val token: String)
