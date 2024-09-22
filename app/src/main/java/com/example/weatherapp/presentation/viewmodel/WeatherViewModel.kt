package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.WeatherUseCase
import com.example.weatherapp.presentation.WeatherUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase) : ViewModel(){

    private val weatherMutableSate by lazy { MutableStateFlow(WeatherUIModel(true, null))  }
    internal val weatherUIModel : StateFlow<WeatherUIModel> = weatherMutableSate

     fun getWeatherDetails(queries: Map<String, String>) {
        viewModelScope.launch {
            weatherUseCase.getWeatherDetails(queries).collect{
                weatherMutableSate.value = WeatherUIModel(false, it)

            }
    }
     }

}