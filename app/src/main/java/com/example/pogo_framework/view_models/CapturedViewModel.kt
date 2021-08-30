package com.example.pogo_framework.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.pogo_framework.helpers.Resource
import com.example.pogo_framework.repositories.BaseRepository
import kotlinx.coroutines.Dispatchers

class CapturedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BaseRepository(application)

    fun getCapturedPokemonList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getCapturedPokemon()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}