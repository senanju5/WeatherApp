package com.example.weatherapp.data.remotedata

import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.data.network.model.WeatherModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val weatherApiService: WeatherApiService) {
    suspend fun getWeatherDetails (queries:Map<String, String>): WeatherModel {
            return weatherApiService.getWeatherDetails(queries)
        }
}