package com.example.weatherapp.domain

import com.example.weatherapp.data.network.model.weathermodel.WeatherModel
import com.example.weatherapp.data.repository.WeatherAppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherAppRepository) {
    suspend fun getWeatherDetails(queries: Map<String, String>): Flow<WeatherModel> {
        return weatherRepository.getWeatherDetails(queries)
    }

}