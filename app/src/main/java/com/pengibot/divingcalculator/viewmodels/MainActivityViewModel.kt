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
import com.pengibot.divingcalculator.business.Calculator
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivityViewModel(application: Application) : ViewModel() {

//    private var _a1 = MutableLiveData<Double>()
//    val a1: LiveData<Double> get() = _a1
//
//    private var _b1 = MutableLiveData<Double>()
//    val b1: LiveData<Double> get() = _b1
//
//    private var _c1 = MutableLiveData<Double>()
//    val c1: LiveData<Double> get() = _c1
//
//    private var _d1 = MutableLiveData<Double>()
//    val d1: LiveData<Double> get() = _d1
//
//    private var _e1 = MutableLiveData<Double>()
//    val e1: LiveData<Double> get() = _e1
//
//    private var _dd1 = MutableLiveData<Double>()
//    val dd1: LiveData<Double> get() = _dd1

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    var diveNumber = MutableLiveData<String>()

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val calculator:Calculator

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                Log.i("MainActivityViewModel", "refreshDataFromRepository")
                _description.value = "Bullshit"
            } catch (networkError: IOException) {
                Log.e("MainActivityViewModel", "networkError in refreshDataFromRepository")
            }
        }
    }

    init {
        Log.i("MainActivityViewModel", "MainActivityViewModel created!")
        refreshDataFromRepository()
        calculator = Calculator()

    }

    fun go() {
        _description.value = calculator.diveDescription(diveNumber.value)
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