package com.example.patitasid.domain.validation

// Shared by every screen that asks for an email or a password (RF-04).
object CredentialRules {
    const val PASSWORD_MIN_LENGTH = 8
    const val NAME_MIN_LENGTH = 2

    private val emailPattern = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""")

    fun isValidEmail(email: String): Boolean = emailPattern.matches(email.trim())

    fun isValidName(name: String): Boolean = name.trim().length >= NAME_MIN_LENGTH

    fun isValidPassword(password: String): Boolean =
        password.length >= PASSWORD_MIN_LENGTH && password.any(Char::isLetter) && password.any(Char::isDigit)
}
