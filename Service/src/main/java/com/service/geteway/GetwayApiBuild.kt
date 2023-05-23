package com.service.geteway

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class GetwayApiBuild constructor(
    private val context: Context
) {
    fun getBaseRetrofit(
        customCOnfigBuilderOkHttp: OkHttpClient.Builder.() -> OkHttpClient.Builder = { this },
        customCOnfigBuilderRetrofit: Retrofit.Builder.() -> Retrofit.Builder = { this }
    ): Lazy<Retrofit> =
        lazy {
            getRetrofitBuilder().apply {
                client(makeBaseOkHttpClient(customCOnfigBuilderOkHttp))
                baseUrl("https://www.googleapis.com/")
                makeScalars()
                makeConverterFactory()
            }.customCOnfigBuilderRetrofit().build()
        }

    private fun Retrofit.Builder.makeConverterFactory() {
        addConverterFactory(GsonConverterFactory.create())
    }

    private fun Retrofit.Builder.makeScalars() {
        addConverterFactory(GsonConverterFactory.create())
    }

    protected open fun makeBaseOkHttpClient(
        okhttpBuilder: OkHttpClient.Builder.() -> OkHttpClient.Builder
    ): OkHttpClient {
        val builder = getOkHttpClientBuilder()
            .okhttpBuilder()
            .makeShowHttpLogging()
            .setTimeout()

        return builder.build()
    }

    protected open fun getOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()

    protected open fun getRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()

    protected open val httpLoggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor()
    }

    private fun OkHttpClient.Builder.makeShowHttpLogging(): OkHttpClient.Builder {
        addInterceptor(
            httpLoggingInterceptor.setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        )
        return this
    }

    private fun OkHttpClient.Builder.setTimeout(): OkHttpClient.Builder {
        connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
        return this
    }

    companion object {
        const val OKHTTP_CONNECT_TIMEOUT = 60L
        const val OKHTTP_READ_TIMEOUT = 60L
        const val OKHTTP_WRITE_TIMEOUT = 60L
    }
}