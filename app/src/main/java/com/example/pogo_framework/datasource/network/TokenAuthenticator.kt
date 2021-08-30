package com.example.pogo_framework.datasource.network

import com.example.pogo_framework.helpers.Constants
import com.example.pogo_framework.helpers.SharedPrefsHelper
import com.example.pogo_framework.models.RefreshTokenResponse
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(private val service: ApiInterface) : Authenticator {
    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {

            val retrofitResponse: retrofit2.Response<RefreshTokenResponse?> =
                service.refreshToken(Constants.EMAIL).execute()
            if (retrofitResponse.isSuccessful) {
                val refreshTokenResponse: RefreshTokenResponse? = retrofitResponse.body()
                val newAccessToken: String = refreshTokenResponse?.token ?: ""
                SharedPrefsHelper.saveAccessToken(refreshTokenResponse?.token)
                SharedPrefsHelper.setTokenExpiryTime(refreshTokenResponse?.expiresAt)
                return response.request().newBuilder()
                    .header("authorization", "Bearer $newAccessToken")
                    .build()
            }
        return null
    }
}
