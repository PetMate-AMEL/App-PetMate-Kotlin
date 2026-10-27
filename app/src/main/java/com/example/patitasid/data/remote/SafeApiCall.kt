package com.example.patitasid.data.remote

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.Response

// Common error handling for every remote repository: the app never crashes on network failures (RNF-18).
suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): SealedResult<T> = guard {
    val response = call()
    val body = response.body()
    when {
        !response.isSuccessful -> SealedResult.Failure(DomainError.Server(response.code()))
        body == null -> SealedResult.Failure(DomainError.Unexpected)
        else -> SealedResult.Success(body)
    }
}

// For endpoints whose body does not matter: any 2xx (204 No Content included) is a success.
suspend fun safeApiCallNoBody(call: suspend () -> Response<Unit>): SealedResult<Unit> = guard {
    val response = call()
    if (response.isSuccessful) SealedResult.Success(Unit) else SealedResult.Failure(DomainError.Server(response.code()))
}

private suspend fun <T> guard(block: suspend () -> SealedResult<T>): SealedResult<T> = try {
    block()
} catch (error: CancellationException) {
    throw error
} catch (error: IOException) {
    SealedResult.Failure(DomainError.Network)
} catch (error: Exception) {
    SealedResult.Failure(DomainError.Unexpected)
}
