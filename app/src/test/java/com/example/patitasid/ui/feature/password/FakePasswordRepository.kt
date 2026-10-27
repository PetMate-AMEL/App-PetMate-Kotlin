package com.example.patitasid.ui.feature.password

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.PasswordRepository

class FakePasswordRepository(
    var requestResult: SealedResult<Unit> = SealedResult.Success(Unit),
    var validateResult: SealedResult<Unit> = SealedResult.Success(Unit),
    var resetResult: SealedResult<Unit> = SealedResult.Success(Unit)
) : PasswordRepository {
    val requestedEmails = mutableListOf<String>()
    val savedPasswords = mutableListOf<String>()

    override suspend fun requestReset(email: String): SealedResult<Unit> {
        requestedEmails += email
        return requestResult
    }

    override suspend fun validateResetLink(token: String): SealedResult<Unit> = validateResult

    override suspend fun resetPassword(token: String, newPassword: String): SealedResult<Unit> {
        savedPasswords += newPassword
        return resetResult
    }
}
