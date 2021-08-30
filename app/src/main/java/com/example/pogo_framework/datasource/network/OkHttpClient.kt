package com.example.pogo_framework.datasource.network

import com.example.pogo_framework.helpers.SharedPrefsHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import java.util.concurrent.TimeUnit


class OkHttpClientInstance() {
    class Builder(var apiInterface: ApiInterface) {
        private val headers = HashMap<String, String>()

        fun addHeader(key: String, value: String): OkHttpClientInstance.Builder {
            headers[key] = value
            return this
        }

        fun build(): OkHttpClient {

            val authenticator = TokenAuthenticator(apiInterface)
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .addInterceptor(
                    Interceptor { chain ->
                        val requestBuilder: Request.Builder = chain.request().newBuilder()
                            .addHeader("accept", "*/*")
                            .addHeader("accept-encoding:gzip", "gzip, deflate")
                            .addHeader("accept-language", "en-US,en;q=0.9")

                        val accessToken = SharedPrefsHelper.getAccessToken()
                        if (accessToken.isNullOrEmpty().not()) {
                            requestBuilder.addHeader("authorization", "Bearer " + accessToken)
                        }
                        chain.proceed(requestBuilder.build())
                    }
                )
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
            authenticator.let {
                if (SharedPrefsHelper.getTokenExpiryTime() < System.currentTimeMillis()) {
                    okHttpClientBuilder.authenticator(it)
                }
            }
            return okHttpClientBuilder.build()
        }
    }
}