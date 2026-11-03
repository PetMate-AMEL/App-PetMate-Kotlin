package com.example.patitasid.domain.usecase

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.AccountRepository
import com.example.patitasid.domain.repository.SessionRepository

// Deletes the account on the server and then clears the saved session (Google Play account deletion policy).
// The session is only cleared after the server confirms, so a failure can be retried while still signed in.
class DeleteAccountUseCase(
    private val accountRepository: AccountRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(): SealedResult<Unit> {
        val session = sessionRepository.current()
        if (session == null) {
            // Nothing on this device points to an account anymore.
            return SealedResult.Success(Unit)
        }
        val result = accountRepository.deleteAccount(accessToken = session.accessToken)
        if (result is SealedResult.Success) sessionRepository.clear()
        return result
    }
}
