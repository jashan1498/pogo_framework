package com.example.pogo_framework.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

@SuppressLint("StaticFieldLeak")
class SharedPrefsHelper {

    companion object {
        const val SHARED_PREFS = "common_app_prefs"
        const val ACCESS_TOKEN = "access_token"
        const val TOKEN_EXPIRY_TIME = "token_expiry_time"
        var context: Context? = null

        fun initPrefs(context: Context) {
            this.context = context
        }

        fun saveAccessToken(accessToken: String?) {
            context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)?.edit()
                ?.putString(ACCESS_TOKEN, accessToken)?.apply()
        }

        fun getAccessToken(): String {
            return context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                ?.getString(ACCESS_TOKEN, "") ?: ""
        }

        fun getTokenExpiryTime(): Long {
            return context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                ?.getLong(TOKEN_EXPIRY_TIME, 0L) ?: 0L
        }

        fun setTokenExpiryTime(accessTokenTTL: Long?) {
            context?.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)?.edit()
                ?.putLong(TOKEN_EXPIRY_TIME, accessTokenTTL ?: 0L)?.apply()
        }
    }
}