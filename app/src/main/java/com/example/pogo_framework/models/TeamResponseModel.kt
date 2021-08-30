package com.example.pogo_framework.models

import com.google.gson.annotations.SerializedName

data class TeamResponseModel(
    @SerializedName("my-team") var pokemonList: List<CapturedModel>
)