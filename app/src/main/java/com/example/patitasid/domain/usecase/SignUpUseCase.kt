package com.example.patitasid.domain.usecase

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.repository.AccountRepository
import com.example.patitasid.domain.repository.SessionRepository

// Creates the account and leaves the session saved, so the app can open "Mis mascotas" right away (RF-02).
class SignUpUseCase(
    private val accountRepository: AccountRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): SealedResult<Session> {
        val result = accountRepository.signUp(name = name.trim(), email = email.trim(), password = password)
        if (result is SealedResult.Success) sessionRepository.save(result.data)
        return result
    }
}
