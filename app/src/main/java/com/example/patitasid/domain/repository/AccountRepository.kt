package com.example.patitasid.domain.repository

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.model.Session

interface AccountRepository {
    // Failure(DomainError.EmailAlreadyUsed) when the email already has an account.
    suspend fun signUp(name: String, email: String, password: String): SealedResult<Session>
}
