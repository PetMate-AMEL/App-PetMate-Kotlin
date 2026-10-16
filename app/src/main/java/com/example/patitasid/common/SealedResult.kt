package com.example.patitasid.common

sealed interface DomainError {
    data object Network : DomainError
    data class Server(val code: Int) : DomainError
    data object Unexpected : DomainError
}

sealed interface SealedResult<out T> {
    data class Success<T>(val data: T) : SealedResult<T>
    data class Failure(val error: DomainError) : SealedResult<Nothing>
}
