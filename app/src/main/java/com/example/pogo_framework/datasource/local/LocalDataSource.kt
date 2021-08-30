package com.example.pogo_framework.datasource.local

import android.content.Context
import com.example.pogo_framework.datasource.local.database.AppDatabase
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.models.Pokemon
import com.example.pogo_framework.models.PokemonList

class LocalDataSource(context: Context) {
    private val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    fun getPokemonList(): PokemonList {
        val pokemonList = appDatabase.pokemonDao().getAllPokemon()
        return PokemonList(pokemonList as ArrayList<Pokemon>?)
    }

    fun savePokemonList(pokemonList: ArrayList<Pokemon>){
        appDatabase.pokemonDao().insertPokemonList(pokemonList)
    }

    fun getPokemon(name:String): Pokemon? {
        return appDatabase.pokemonDao().getPokemon(name)
    }

    fun getPokemon(pokemonId:Int): Pokemon? {
        return appDatabase.pokemonDao().getPokemon(pokemonId)
    }

    fun saveTeam(capturedModelList:ArrayList<CapturedModel>){
        appDatabase.pokemonTeamTao().insertPokemonList(capturedModelList)
    }

    fun getTeam() : ArrayList<CapturedModel>{
        return appDatabase.pokemonTeamTao().getTeamPokemon() as ArrayList<CapturedModel>
    }
}