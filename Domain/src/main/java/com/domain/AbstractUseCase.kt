package com.domain

import com.repository.error.tryConvertError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class AbstractUseCase<in PARAM, out RESPONSE> {

    protected abstract suspend fun execute(param: PARAM): RESPONSE

    open suspend fun onError(throwable: Throwable): ResultWrapper.Failure =
        throwable.tryConvertError<Any>().asFailure()

    open operator fun invoke(
        value: PARAM
    ): Flow<ResultWrapper<RESPONSE>> = flow {
        emit(ResultWrapper.Loading)
        try {
            val result = execute(value)
            emit(result.asSuccess())
        } catch (e: Throwable) {
            emit(onError(e))
        } finally {
            emit(ResultWrapper.DismissLoading)
        }
    }

}