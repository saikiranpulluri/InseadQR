package com.konbini.inseadqr.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

object OkInterceptor {
    private var mClient: OkHttpClient? = null

    /**
     * Don't forget to remove Interceptors (or change Logging Level to NONE)
     * in production! Otherwise people will be able to see your request and response on Log Cat.
     */
    val client: OkHttpClient
        @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
        get() {
            if (mClient == null) {
                val httpBuilder = OkHttpClient.Builder()
                httpBuilder
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(35, TimeUnit.SECONDS)
                val interceptor = HttpLoggingInterceptor {
                    Log.i("Insead QR", it)
                    // LogUtils.logApi(it)
                }
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                httpBuilder.addInterceptor(interceptor)  /// show all JSON in logCat
                mClient = httpBuilder.protocols(listOf(Protocol.HTTP_1_1))
                    .build()
            }
            return mClient!!
        }
}