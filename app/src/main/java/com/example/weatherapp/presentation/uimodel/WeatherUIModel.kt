package com.example.weatherapp.presentation.uimodel

import com.example.weatherapp.data.model.WeatherUIDetails

data class WeatherUIModel (val isLoading: Boolean, val weatherDetails: WeatherUIDetails?, val isError:Boolean )