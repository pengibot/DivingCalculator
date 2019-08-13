package com.pengibot.divingcalculator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.pengibot.divingcalculator.R
import com.pengibot.divingcalculator.databinding.ActivityMainBinding
import com.pengibot.divingcalculator.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        viewModelFactory = MainActivityViewModel.Factory(this.application)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainActivityViewModel::class.java)

        binding.viewModel = viewModel
    }
}
