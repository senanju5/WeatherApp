package com.example.weatherapp.data.remotedata

import com.example.weatherapp.data.network.GeoCodeApiService
import com.example.weatherapp.data.network.WeatherApiService
import com.example.weatherapp.data.network.model.geocodemodel.GeoCodes
import com.example.weatherapp.data.network.model.weathermodel.WeatherModel
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val weatherApiService: WeatherApiService, private val geoApiService: GeoCodeApiService) {
    suspend fun getWeatherDetails (queries:Map<String, String>): WeatherModel {
            return weatherApiService.getWeatherDetails(queries)
        }
    suspend fun getGeoCode (queries:Map<String, String>): GeoCodes {
        return geoApiService.getGeoCode(queries)
    }
}