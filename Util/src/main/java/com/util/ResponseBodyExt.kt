package com.util

import retrofit2.Response
import okhttp3.ResponseBody

fun Response<*>.cloneErrorResponseBody(): ResponseBody? {
    val responseBody: ResponseBody? = errorBody()
    val bufferedSource = responseBody?.source()
    bufferedSource?.request(Long.MAX_VALUE)
    return safeLet(
        responseBody?.contentType(),
        bufferedSource?.buffer()?.clone(),
        responseBody?.contentLength()
    ) { contentType, bufferClone, length ->
        ResponseBody.create(contentType, length, bufferClone)
    }
}

fun ResponseBody.cloneErrorResponseBody(): ResponseBody? {
    val bufferedSource = source()
    bufferedSource.request(Long.MAX_VALUE)
    return safeLet(
        contentType(),
        bufferedSource.buffer().clone(),
        contentLength()
    ) { contentType, bufferClone, length ->
        ResponseBody.create(contentType, length, bufferClone)
    }
}