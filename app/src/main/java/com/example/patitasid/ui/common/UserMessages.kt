package com.example.patitasid.ui.common

import com.example.patitasid.common.DomainError

// Error texts shared by every screen (RNF-14, RNF-18).
object UserMessages {
    const val NETWORK = "No hay conexión a internet. Revisa tu red e inténtalo de nuevo."
    const val SERVER = "El servidor no está disponible en este momento. Inténtalo más tarde."
    const val EMAIL_ALREADY_USED = "Este correo ya está registrado."
}

fun DomainError.toUserMessage(): String = when (this) {
    DomainError.Network -> UserMessages.NETWORK
    DomainError.EmailAlreadyUsed -> UserMessages.EMAIL_ALREADY_USED
    is DomainError.Server, DomainError.Unexpected, DomainError.ExpiredLink -> UserMessages.SERVER
}
