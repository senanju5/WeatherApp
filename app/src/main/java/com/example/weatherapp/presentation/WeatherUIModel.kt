package com.example.weatherapp.presentation

import com.example.weatherapp.data.network.model.WeatherModel

data class WeatherUIModel (val isLoading: Boolean, val weatherDetails: WeatherModel? )