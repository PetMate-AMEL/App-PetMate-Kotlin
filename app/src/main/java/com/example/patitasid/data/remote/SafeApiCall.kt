package com.example.patitasid.data.remote

import com.example.patitasid.common.DomainError
import com.example.patitasid.common.SealedResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.Response

// Common error handling for every remote repository: the app never crashes on network failures (RNF-18).
suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): SealedResult<T> = try {
    val response = call()
    val body = response.body()
    when {
        !response.isSuccessful -> SealedResult.Failure(DomainError.Server(response.code()))
        body == null -> SealedResult.Failure(DomainError.Unexpected)
        else -> SealedResult.Success(body)
    }
} catch (error: CancellationException) {
    throw error
} catch (error: IOException) {
    SealedResult.Failure(DomainError.Network)
} catch (error: Exception) {
    SealedResult.Failure(DomainError.Unexpected)
}
