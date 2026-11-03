package com.example.patitasid.ui.feature.signup

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.repository.AccountRepository

class FakeAccountRepository(
    var signUpResult: SealedResult<Session> = SealedResult.Success(
        Session(accessToken = "acceso", refreshToken = "renovacion", hasBusiness = false)
    )
) : AccountRepository {
    val signedUpEmails = mutableListOf<String>()
    val signedUpNames = mutableListOf<String>()
    var deleteResult: SealedResult<Unit> = SealedResult.Success(Unit)
    val deletedTokens = mutableListOf<String>()

    override suspend fun signUp(name: String, email: String, password: String): SealedResult<Session> {
        signedUpNames += name
        signedUpEmails += email
        return signUpResult
    }

    override suspend fun deleteAccount(accessToken: String): SealedResult<Unit> {
        deletedTokens += accessToken
        return deleteResult
    }
}
