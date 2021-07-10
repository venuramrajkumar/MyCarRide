package com.raj.mycarride.rest

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ConnectivityInterceptor: Interceptor {

    var context: Context

    @Inject
    constructor(context: Context) {
        this.context = context
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return makeRequest(chain)
    }


    fun makeRequest(chain: Interceptor.Chain): Response {
        var response: Response

        if (isconnected()) {
            response = chain.proceed(chain.request())

        } else {
            throw NoConnectivityException()
        }


        return response
    }

    fun isconnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
    class NoConnectivityException:Throwable(){
        override val message: String?
            get() = "No network available, please check your WiFi or Data connection"
    }

