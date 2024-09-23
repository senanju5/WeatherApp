package com.example.weatherapp.presentation.uimodel

import com.example.weatherapp.data.model.WeatherUIDetails

/**
 * UI Model for Weather Details state management
 */
data class WeatherUIModel (val isLoading: Boolean, val weatherDetails: WeatherUIDetails?, val isError:Boolean )