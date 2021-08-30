package com.example.pogo_framework.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pogo_framework.models.PokemonList
import com.example.pogo_framework.repositories.BaseRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BaseRepository(application)

    fun getPokemonList(): LiveData<PokemonList?> {
        return repository.getPokemonList()
    }

}