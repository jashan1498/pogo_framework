package com.example.pogo_framework.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.pogo_framework.helpers.Resource
import com.example.pogo_framework.repositories.BaseRepository
import kotlinx.coroutines.Dispatchers

class CommonViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BaseRepository(application)

    fun getPokemon(name: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val pokemon = repository.getPokemon(name.lowercase())
            pokemon?.let {
                emit(Resource.success(data = repository.getPokemonData(pokemon.id)))
            } ?: emit(Resource.loading(data = null))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getPokemon(pokemonId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val pokemon = repository.getPokemon(pokemonId)
            pokemon?.let {
                emit(Resource.success(data = repository.getPokemonData(pokemon.id)))
            } ?: emit(Resource.loading(data = null))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}