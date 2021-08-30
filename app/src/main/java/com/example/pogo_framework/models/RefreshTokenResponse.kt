package com.example.pogo_framework.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("token") var token: String,
    @SerializedName("expiresAt") var expiresAt: Long
)