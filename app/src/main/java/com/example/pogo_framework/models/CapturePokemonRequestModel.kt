package com.example.pogo_framework.models

import com.google.gson.annotations.SerializedName

data class CapturePokemonRequestModel(
    @SerializedName("pokemon") var pokemon: ActivityPokemon
)