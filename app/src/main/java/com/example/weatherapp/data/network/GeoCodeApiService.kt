package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.model.geocodemodel.GeoCodes
import com.example.weatherapp.utils.ApiConfig
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Interface for GeoCode API Service
 */
interface GeoCodeApiService {
    @GET(ApiConfig.GEO_ENDPOINT)
    suspend fun getGeoCode(@QueryMap queries: Map<String, String>): GeoCodes
}