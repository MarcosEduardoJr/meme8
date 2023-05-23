package com.common.base.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.common.R
import com.common.typealiases.OnClickItem
import com.common.typealiases.OnCompletionBaseViewModel
import com.common.typealiases.OnErrorBaseViewModel
import com.common.typealiases.OnSuccessBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import com.domain.Error
import com.domain.ResultWrapper
import com.common.base.viewmodel.CommonMutableVariableViewModel
import com.util.mapper.AbstractMapper

//abstract class BaseViewModel constructor(): ViewModel() {
//}

abstract class BaseViewModel : CoroutinesConfigViewModel() {

    var onClickItem: OnClickItem? = null

    open fun initializer(onClickItem: OnClickItem) {
        this.onClickItem = onClickItem
    }

    open fun onItemClicked(v: View) {
        onClickItem?.invoke(v)
    }

    /**
     * this method allows to change the way the usecase is
     * executed, it is possible to get instant loads and error
     * messages handled or customized when passing a usecase
     *
     * @param onError allows you to customize the error message
     * @param showLoadingFlag This parameter allows you to decide whether to
     * show a load or not (Optional)
     */
    fun <T : Any> Flow<ResultWrapper<T>>.exec(
        onError: OnErrorBaseViewModel = { _, err, _ -> err },
        showLoadingFlag: Boolean = true
    ): LiveData<T> = mapNotNull {
        when (val result = it) {
            is ResultWrapper.Loading -> {
                showLoadingWithFlag(showLoadingFlag)
                null
            }
            is ResultWrapper.Failure -> {
                resolveResultWrapperFailure(result, onError)
                null
            }
            is ResultWrapper.Success<T> -> result.value
            is ResultWrapper.DismissLoading -> {
                hideLoadingWithFlag(showLoadingFlag)
                null
            }
        }
    }.catch {
        hideLoadingWithFlag(showLoadingFlag)
      //todo log error
    }.asLiveData(coroutineContext)

    /**
     * this method allows you to use an AbstractUseCase to
     * return a simple value, without needing LiveData
     *
     * @param onError allows you to customize the error message (Optional)
     * @param onSuccessBaseViewModel Allows you to use your return from
     * an endpoint or a database, in your view (Optional)
     * @param onCompletionBaseViewModel It is equivalent to the try/catch finally (Optional)
     * @param showLoadingFlag This parameter allows you to decide whether to
     * show a load or not (Optional)
     */
    open fun <T : Any> Flow<ResultWrapper<T>>.singleExec(
        onError: OnErrorBaseViewModel = { _, err, _ -> err },
        onSuccessBaseViewModel: OnSuccessBaseViewModel<T>? = null,
        onCompletionBaseViewModel: OnCompletionBaseViewModel? = null,
        showLoadingFlag: Boolean = true
    ) = onEach {
        when (val result = it) {
            is ResultWrapper.Loading -> {
                showLoadingWithFlag(showLoadingFlag)
            }
            is ResultWrapper.Failure -> {
                resolveResultWrapperFailure(result, onError)
            }
            is ResultWrapper.Success<T> ->
                onSuccessBaseViewModel?.invoke(result.value)
            is ResultWrapper.DismissLoading -> {
                hideLoadingWithFlag(showLoadingFlag)
            }
        }
    }.onCompletion {
        if (it?.cause != null) {
          //todo log  Timber.e(it)
        }
        onCompletionBaseViewModel?.invoke()
    }.catch {
        hideLoadingWithFlag(showLoadingFlag)
        //todo log   Timber.e(it)
    }.launchIn(CoroutineScope(coroutineContext))

    open fun <T : Any> singleExecParametter(
        flow: Flow<ResultWrapper<T>>,
        onError: OnErrorBaseViewModel = { _, err, _ -> err },
        onSuccessBaseViewModel: OnSuccessBaseViewModel<T>? = null,
        onCompletionBaseViewModel: OnCompletionBaseViewModel? = null,
        showLoadingFlag: Boolean = true
    ) = flow.singleExec(
        onError,
        onSuccessBaseViewModel,
        onCompletionBaseViewModel,
        showLoadingFlag
    )

    fun <PARAMETER, RESULT> LiveData<PARAMETER>.map(abstractMap: AbstractMapper<PARAMETER, RESULT>): LiveData<RESULT> {
        return Transformations.map(this, abstractMap::map)
    }
    private suspend fun resolveResultWrapperFailure(
        result: ResultWrapper.Failure,
        onError: OnErrorBaseViewModel
    ) {
        val errorResolved = when (val error = result.error) {
            is Error.Server ->   "Erro no servidor"//makeOnError(
             //   statusCode = error.code,
               // error =  "Erro no servidor",
              //  throwable = error.cause,
              //  onError = onError
           // )
            is Error.Business ->  "Entre em contato com o suporte"
                //makeOnError(
              //  error = "Entre em contato com o suporte",
              //  throwable = error.cause,
             //   onError = onError
           // )
            is Error.UnkownException -> error.cause?.message.orEmpty()?:""//makeOnError(
              //  throwable = error.cause,
             //   onError = onError
           // )
            is Error.NetworkException -> "Problemas de conexão"
            //makeOnError(
              //  error = "Problemas de conexão",
              //  throwable = error.cause,
             //   onError = onError
           // )
            else -> "Entre em contato com o suporte"
        }
        message.value = errorResolved
    }
//todo message generic object
    private suspend fun makeOnError(
        statusCode: Int? = null,
        error: String? = null,
        throwable: Throwable? = null,
        onError: OnErrorBaseViewModel
    ): Unit =
        onError(
            statusCode,
            error ?: throwable?.message.orEmpty(),
            throwable
        )

    private fun hideLoadingWithFlag(showLoadingFlag: Boolean) {
        if (showLoadingFlag) {
            hideLoading()
        }
    }

    private fun showLoadingWithFlag(showLoadingFlag: Boolean) {
        if (showLoadingFlag) {
            showLoading()
        }
    }

    open fun hideLoading() {
        showLoading.value = false
    }

    open fun showLoading() {
        showLoading.value = true
    }
}