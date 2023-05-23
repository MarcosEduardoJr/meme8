package com.domain

import com.repository.error.ServerException
import com.util.isNetworkConnected
import java.net.HttpURLConnection

fun <T> T.asSuccess(): ResultWrapper.Success<T> = ResultWrapper.Success(this)

suspend fun Throwable.asFailure(): ResultWrapper.Failure = when (this) {
    is ServerException -> {
        when (code) {
            in HttpURLConnection.HTTP_BAD_REQUEST until HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                resultResolve()
            }
            else -> ResultWrapper.Failure(Error.UnkownException(this))
        }
    }
    else -> {
        if (isNetworkConnected()) {
            ResultWrapper.Failure(Error.UnkownException(this))
        } else {
            ResultWrapper.Failure(Error.NetworkException(this))
        }
    }
}


private fun ServerException.resultResolve(): ResultWrapper.Failure =
    ResultWrapper.Failure(
        Error.Server(
            code = code,
            message = "Tente novamente mais tarde",
            cause = this
        )
    )
