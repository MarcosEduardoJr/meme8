package com.domain

import androidx.lifecycle.LiveData

abstract class AbstractiLiveDataUseCase<in PARAM, RESPONSE> {
    protected abstract fun execute(param: PARAM): LiveData<RESPONSE>

    open operator fun invoke(value: PARAM): LiveData<RESPONSE> = execute(value)
}