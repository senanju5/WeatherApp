package com.example.weatherapp.domain

import com.example.weatherapp.data.network.model.WeatherModel
import com.example.weatherapp.data.repository.WeatherDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherDetailsRepository) {
    suspend fun getWeatherDetails(queries: Map<String, String>): Flow<WeatherModel> {
        return weatherRepository.getWeatherDetails(queries)
    }

}