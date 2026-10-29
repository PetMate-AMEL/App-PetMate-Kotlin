package com.example.patitasid.data.repository

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.data.remote.AccountApiService
import com.example.patitasid.data.remote.SessionResponse
import com.example.patitasid.data.remote.SignUpRequest
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Response

class AccountRemoteRepositoryTest {
    private class FakeApi(
        private val code: Int,
        private val body: SessionResponse? = null
    ) : AccountApiService {
        override suspend fun signUp(body: SignUpRequest): Response<SessionResponse> =
            if (code in 200..299 && this.body != null) {
                Response.success(code, this.body)
            } else {
                Response.error(code, "".toResponseBody())
            }
    }

    private fun repositoryOf(code: Int, body: SessionResponse? = null) = AccountRemoteRepository(FakeApi(code, body))

    @Test
    fun `a created account returns the session`() = runTest {
        val body = SessionResponse(accessToken = "acceso", refreshToken = "renovacion", tieneNegocio = true)

        val result = repositoryOf(200, body).signUp("Ana", "ana@correo.com", "mascota123")

        val session = (result as SealedResult.Success).data
        assertEquals("acceso", session.accessToken)
        assertEquals("renovacion", session.refreshToken)
        assertEquals(true, session.hasBusiness)
    }

    @Test
    fun `conflict means the email already has an account`() = runTest {
        val result = repositoryOf(409).signUp("Ana", "registrado@correo.com", "mascota123")

        assertEquals(SealedResult.Failure(DomainError.EmailAlreadyUsed), result)
    }

    @Test
    fun `server failure stays a server error`() = runTest {
        val result = repositoryOf(500).signUp("Ana", "ana@correo.com", "mascota123")

        assertEquals(SealedResult.Failure(DomainError.Server(500)), result)
    }
}
