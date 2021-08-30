package com.example.pogo_framework.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.pogo_framework.helpers.Resource
import com.example.pogo_framework.repositories.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class TeamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BaseRepository(application)
    private var teamInfoMutableLiveData: MutableLiveData<Resource<Any>> = MutableLiveData()
    var teamInfoLiveData: LiveData<Resource<Any>> = teamInfoMutableLiveData

    fun getTeamInfo(){
        GlobalScope.launch {
            try {
                repository.getMyTeam(teamInfoMutableLiveData)
            } catch (exception: Exception) {
            }
        }
    }
}