package com.example.pogo_framework.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pogo_framework.datasource.local.LocalDataSource
import com.example.pogo_framework.datasource.network.RetrofitClient
import com.example.pogo_framework.helpers.Resource
import com.example.pogo_framework.helpers.Status
import com.example.pogo_framework.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class BaseRepository(private val context: Context) {
    private val localDataSource = LocalDataSource(context)

    fun getPokemonList(): LiveData<PokemonList?> {
        val mutableLiveData: MutableLiveData<PokemonList> = MutableLiveData()
        GlobalScope.launch {
            var pokemonList = localDataSource.getPokemonList()
            if (pokemonList.results.isNullOrEmpty()) {
                pokemonList =
                    RetrofitClient.getInstance()
                        .getPokemonList("https://pokeapi.co/api/v2/pokemon?limit=1118")
                pokemonList.results?.let { savePokemonListToDb(it) }
                mutableLiveData.postValue(pokemonList)
            } else {
                mutableLiveData.postValue(pokemonList)
            }
        }
        return mutableLiveData
    }

    suspend fun getProfileActivity(): ActivityModelResponse {
        return RetrofitClient.getInstance().getProfileActivity()
    }

    suspend fun capturePokemon(): ActivityModelResponse {
        return RetrofitClient.getInstance().getProfileActivity()
    }


    suspend fun getMyTeam(capturedList: MutableLiveData<Resource<Any>>) {

        GlobalScope.launch {
            var pokemonList = localDataSource.getTeam()
            capturedList.postValue(Resource(Status.SUCCESS, pokemonList, ""))
            try {
                val networkPokemonList =
                    RetrofitClient.getInstance()
                        .getMyTeam()
                val saveList = ArrayList<CapturedModel>()
                networkPokemonList.forEach {
                    val pokemon = pokemonList.find { capturedModel ->
                        it.id == capturedModel.id && it.capturedAt == capturedModel.capturedAt && it.capturedLatAt == capturedModel.capturedLatAt && it.capturedLongAt == capturedModel.capturedLongAt
                    }
                    if (pokemon == null) {
                        saveList.add(it)
                    }
                }
                saveTeam(saveList)
                capturedList.postValue(Resource(Status.SUCCESS, networkPokemonList, ""))
            } catch (e: Exception) {

            }
        }
    }

    fun getPokemon(name: String): Pokemon? {
        return localDataSource.getPokemon(name)
    }

    fun getPokemon(pokemonId: Int): Pokemon? {
        return localDataSource.getPokemon(pokemonId)
    }

    private fun saveTeam(capturedList: ArrayList<CapturedModel>) {
        localDataSource.saveTeam(capturedList)
    }

    suspend fun getCapturedPokemon(): ArrayList<CapturedModel> {
        return RetrofitClient.getInstance().getCapturedPokemon()
    }

    private fun savePokemonListToDb(pokemonList: ArrayList<Pokemon>) {
        pokemonList.let {
            localDataSource.savePokemonList(pokemonList)
        }
    }

    suspend fun getPokemonData(id: Int): PokemonData {
        return RetrofitClient.getInstance()
            .getPokemonData("https://pokeapi.co/api/v2/pokemon/${id}")
    }
}