package com.example.patitasid.data.remote

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import retrofit2.Response

class SafeApiCallTest {
    @Test
    fun `successful response returns Success`() = runTest {
        assertEquals(SealedResult.Success(Unit), safeApiCall { Response.success(Unit) })
    }

    @Test
    fun `no content without body is still a success`() = runTest {
        assertEquals(SealedResult.Success(Unit), safeApiCallNoBody { Response.success<Unit>(204, null) })
    }

    @Test
    fun `no internet returns Network error instead of crashing`() = runTest {
        val result = safeApiCall<Unit> { throw IOException("offline") }
        assertEquals(SealedResult.Failure(DomainError.Network), result)
    }

    @Test
    fun `http error returns Server error with code`() = runTest {
        val result = safeApiCall<Unit> { Response.error(503, "".toResponseBody()) }
        assertEquals(SealedResult.Failure(DomainError.Server(503)), result)
    }
}
