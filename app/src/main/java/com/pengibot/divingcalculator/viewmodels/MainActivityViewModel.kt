package com.pengibot.divingcalculator.viewmodels

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import android.app.Application
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pengibot.divingcalculator.R
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivityViewModel(application: Application) : ViewModel() {

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                Log.i("MainActivityViewModel", "refreshDataFromRepository")
                _message.value = "Boring"
            } catch (networkError: IOException) {
                Log.e("MainActivityViewModel", "networkError in refreshDataFromRepository")
            }
        }
    }

    init {
        Log.i("MainActivityViewModel", "MainActivityViewModel created!")
        refreshDataFromRepository()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                return MainActivityViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}