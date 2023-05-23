package com.service.client

import android.content.Context
import com.service.endpoint.GoogleEndpoint
import com.service.geteway.GetwayApiBuild
import retrofit2.Retrofit
import javax.inject.Inject

class GoogleClient constructor(
    context: Context
) : GetwayApiBuild(context){

    private val retrofit: Retrofit by getBaseRetrofit()

    val googleEndpoint : GoogleEndpoint by lazy {
        retrofit.create(GoogleEndpoint::class.java)
    }
}