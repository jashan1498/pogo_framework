package com.example.pogo_framework.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityModelResponse(
    @SerializedName("friends")
    var friends: ArrayList<Friends>? = null,

    @SerializedName("foes")
    var foes: ArrayList<Friends>? = null
)

data class ActivityPokemon(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("captured_at") var capturedAt: String,
    @SerializedName("lat") var lat: Double,
    @SerializedName("long") var long: Double
) : Serializable

data class Friends(

    @SerializedName("pokemon") var pokemon: ActivityPokemon,
    @SerializedName("name") var name: String

) : Serializable