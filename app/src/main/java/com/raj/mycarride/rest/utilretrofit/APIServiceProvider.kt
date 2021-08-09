package com.raj.mycarride.rest.utilretrofit

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class APIServiceProvider private constructor(
    baseUrl: String,
    readTimeout: Long,
    connectTimeout: Long,
    logLevel: HttpLoggingInterceptor.Level
) {
    private val okHttpClient: OkHttpClient
    private val retrofit: Retrofit? = null
    private val loggingInterceptor: HttpLoggingInterceptor
    var apiInterface: APIInterface

    fun getUserData(): Call<UserResponse> {
        return apiInterface.getUsers()    }

    companion object {
        private var apiServiceProvider: APIServiceProvider? = null

        fun getApiServiceProvider(
            baseUrl: String,
            readTimeout: Long,
            connectTimeout: Long,
            logLevel: HttpLoggingInterceptor.Level
        ): APIServiceProvider? {
            if (apiServiceProvider == null) {
                apiServiceProvider =
                    APIServiceProvider(baseUrl, readTimeout, connectTimeout, logLevel)
            }
            return apiServiceProvider
        }
    }

    init {
        loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(logLevel)
        okHttpClient = OkHttpClient.Builder()
            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .build()
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)
    }
}
