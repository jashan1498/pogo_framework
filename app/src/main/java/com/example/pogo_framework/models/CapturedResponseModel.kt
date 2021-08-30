package com.example.pogo_framework.models

import com.google.gson.annotations.SerializedName

data class CapturedResponseModel(
    @SerializedName("captured") var captured: ArrayList<CapturedModel>
)