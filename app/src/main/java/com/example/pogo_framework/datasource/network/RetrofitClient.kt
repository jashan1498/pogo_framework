package com.example.pogo_framework.datasource.network

import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        var retrofit: ApiInterface? = null
        var BaseUrl = "https://us-central1-samaritan-android-assignment.cloudfunctions.net/"

        fun getInstance(): ApiInterface {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .client(OkHttpClientInstance.Builder(buildTokenApi()).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiInterface::class.java)
            }
            return retrofit!!
        }

        private fun buildTokenApi(): ApiInterface {
            return Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(getRetrofitClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }

        private fun getRetrofitClient(authenticator: Authenticator? = null): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(chain.request().newBuilder().also {
                        it.addHeader("Accept", "application/json")
                    }.build())
                }.also { client ->
                    authenticator?.let { client.authenticator(it) }
                }.build()
        }

    }
}