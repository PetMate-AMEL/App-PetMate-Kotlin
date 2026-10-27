package com.example.patitasid.domain.validation

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CredentialRulesTest {
    @Test
    fun `email needs user, at sign and domain`() {
        assertTrue(CredentialRules.isValidEmail("ana@correo.com"))
        assertTrue(CredentialRules.isValidEmail("  ana@correo.com.mx "))
        assertFalse(CredentialRules.isValidEmail("ana@correo"))
        assertFalse(CredentialRules.isValidEmail("ana correo@x.com"))
        assertFalse(CredentialRules.isValidEmail(""))
    }

    @Test
    fun `password needs 8 characters with letters and numbers`() {
        assertTrue(CredentialRules.isValidPassword("Firulais1"))
        assertFalse(CredentialRules.isValidPassword("Firu1"))
        assertFalse(CredentialRules.isValidPassword("solamenteletras"))
        assertFalse(CredentialRules.isValidPassword("12345678"))
    }

    @Test
    fun `name cannot be empty or a single letter`() {
        assertTrue(CredentialRules.isValidName("Ana"))
        assertTrue(CredentialRules.isValidName("  Ana  "))
        assertFalse(CredentialRules.isValidName(""))
        assertFalse(CredentialRules.isValidName("   "))
        assertFalse(CredentialRules.isValidName("A"))
    }
}
