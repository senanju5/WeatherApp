package com.example.weatherapp.data.repository

import com.example.weatherapp.data.network.model.WeatherModel
import com.example.weatherapp.data.remotedata.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherDetailsRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
        suspend fun getWeatherDetails(queries: Map<String, String>): Flow<WeatherModel> = flow {
            emit(remoteDataSource.getWeatherDetails(queries))
        }.flowOn(Dispatchers.IO)

}