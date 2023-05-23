package com.repository.error

import com.util.castOrNull
import retrofit2.HttpException

inline fun <reified T : Any> Throwable.tryConvertError(): Throwable =
    asServerExceptionOrNull<T>() ?: this

inline fun <reified T : Any> Throwable.asServerExceptionOrNull(): ServerException? {
    val httpException = castOrNull<HttpException>()

    return httpException?.let { http ->
        ServerException(http.code())
    }
}