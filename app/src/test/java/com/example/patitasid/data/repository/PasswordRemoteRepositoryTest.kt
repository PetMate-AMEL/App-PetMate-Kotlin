package com.example.patitasid.data.repository

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import com.example.patitasid.data.remote.ForgotPasswordRequest
import com.example.patitasid.data.remote.PasswordApiService
import com.example.patitasid.data.remote.ResetLinkRequest
import com.example.patitasid.data.remote.ResetPasswordRequest
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Response

class PasswordRemoteRepositoryTest {
    private class FakeApi(private val code: Int) : PasswordApiService {
        private fun respond(): Response<Unit> =
            if (code in 200..299) Response.success(code, Unit) else Response.error(code, "".toResponseBody())

        override suspend fun requestReset(body: ForgotPasswordRequest) = respond()
        override suspend fun validateResetLink(body: ResetLinkRequest) = respond()
        override suspend fun resetPassword(body: ResetPasswordRequest) = respond()
    }

    @Test
    fun `gone token means expired link`() = runTest {
        val result = PasswordRemoteRepository(FakeApi(410)).validateResetLink("abc")
        assertEquals(SealedResult.Failure(DomainError.ExpiredLink), result)
    }

    @Test
    fun `unknown token on save means expired link`() = runTest {
        val result = PasswordRemoteRepository(FakeApi(404)).resetPassword("abc", "Firulais2026")
        assertEquals(SealedResult.Failure(DomainError.ExpiredLink), result)
    }

    @Test
    fun `no content counts as success`() = runTest {
        assertEquals(SealedResult.Success(Unit), PasswordRemoteRepository(FakeApi(204)).requestReset("ana@correo.com"))
    }

    @Test
    fun `server failure stays a server error`() = runTest {
        val result = PasswordRemoteRepository(FakeApi(500)).validateResetLink("abc")
        assertEquals(SealedResult.Failure(DomainError.Server(500)), result)
    }
}
