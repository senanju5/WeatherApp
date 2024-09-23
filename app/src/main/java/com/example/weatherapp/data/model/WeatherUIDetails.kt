package com.example.weatherapp.data.model

/**
 * UI model for weather details
 * This model is used to display the weather details in the UI
 */
data class WeatherUIDetails(
    var dateTime: String = "",
    var temperature: String = "0",
    var cityAndCountry: String = "",
    var weatherIcon: String = "",
    var weatherIconDesc: String = "",
    var humidity: String = "",
    var pressure: String = "",
    var visibility: String = "",
    var sunrise: String = "",
    var sunset: String = "",
    var lat: String = "",
    var lon: String = "",
)