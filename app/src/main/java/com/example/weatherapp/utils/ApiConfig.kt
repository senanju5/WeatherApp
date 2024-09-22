package com.example.weatherapp.utils

import com.example.weatherapp.utils.ApiConfig.API_KEY

internal  object ApiConfig {
        const val BASE_URL = "https://api.openweathermap.org"
        const val API_KEY = "6b752f3d405f987d00a62dd42d7b4017"
        const val WEATHER_ENDPOINT = "/data/2.5/weather"
}

fun getQuery():HashMap<String,String> {
    val query = HashMap<String, String>()
    query["lat"] = "44.34"
    query["lon"] = "10.99"
    query[ "appid"] = API_KEY
    return query
}