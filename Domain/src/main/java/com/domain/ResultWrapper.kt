package com.domain

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val error: Error) : ResultWrapper<Nothing>()
    object Loading : ResultWrapper<Nothing>()
    object DismissLoading : ResultWrapper<Nothing>()
}

sealed class Error {
    data class Server(
        val code: Int,
        val message: String? = null,
        val cause: Throwable? = null
    ) : Error()

    data class Business(
        val message: String? = null,
        val cause: Throwable? = null
    ) : Error()

    data class Logout(
        val cause: Throwable? = null
    ) : Error()

    data class NetworkException(
        val cause: Throwable? = null
    ) : Error()

    data class UnkownException(
        val cause: Throwable? = null
    ) : Error()
}
