package com.example.pogo_framework.datasource.network

import com.example.pogo_framework.models.*
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET
    suspend fun getPokemonList(@Url url: String): PokemonList

    @GET("/activity")
    suspend fun getProfileActivity(): ActivityModelResponse

    @GET("/capture")
    suspend fun capturePokemon(@Body capturePokemonRequestModel: CapturePokemonRequestModel): ActivityModelResponse

    @GET("/captured")
    suspend fun getCapturedPokemon(): ArrayList<CapturedModel>

    @GET
    suspend fun getPokemonData(@Url url: String): PokemonData

    @GET("/my-team")
    suspend fun getMyTeam(): ArrayList<CapturedModel>

    @POST("token")
    fun refreshToken(@Query("email") email: String?): Call<RefreshTokenResponse>
}