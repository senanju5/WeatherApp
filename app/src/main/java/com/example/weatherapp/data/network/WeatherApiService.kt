package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.model.weathermodel.WeatherModel
import com.example.weatherapp.utils.ApiConfig
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Interface for Weather API service
 */
interface WeatherApiService {
    @GET(ApiConfig.WEATHER_ENDPOINT)
    suspend fun getWeatherDetails(@QueryMap queries: Map<String, String>): WeatherModel
}