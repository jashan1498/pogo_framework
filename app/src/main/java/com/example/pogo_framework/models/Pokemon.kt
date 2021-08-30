package com.example.pogo_framework.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemon_list")
class Pokemon {
    @PrimaryKey(autoGenerate = true)
    public var id: Int = 0

    @SerializedName("name")
    @ColumnInfo(name = "name")
    public var name: String = ""

    @SerializedName("url")
    @ColumnInfo(name = "url")
    public var url: String = ""

    @Transient
    public var type: String = ""

    @Transient
    public var imageUrl: String = ""
}

data class PokemonList(
    @SerializedName("results")
    val results: ArrayList<Pokemon>? = null
)

data class PokemonData(
    @SerializedName("base_experience") var baseExperience: Int,
    @SerializedName("height") var height: Int,
    @SerializedName("id") var id: Int,
    @SerializedName("is_default") var isDefault: Boolean,
    @SerializedName("location_area_encounters") var locationAreaEncounters: String,
    @SerializedName("moves") var moves: List<Moves>,
    @SerializedName("name") var name: String,
    @SerializedName("order") var order: Int,
    @SerializedName("sprites") var sprites: Sprites,
    @SerializedName("weight") var weight: Int,
    @SerializedName("types") var types : List<Types>,
    ) {

    data class Moves(
        @SerializedName("move") var move: Move,
    )

    data class Types (

        @SerializedName("slot") var slot : Int,
        @SerializedName("type") var type : Type

    )

    data class Type (

        @SerializedName("name") var name : String,
        @SerializedName("url") var url : String

    )

    data class Move(
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    )

    data class Sprites(
        @SerializedName("back_default") var backDefault: String,
        @SerializedName("front_default") var frontDefault: String,
    )
}
