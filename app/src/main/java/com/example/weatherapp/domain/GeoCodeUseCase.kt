package com.example.weatherapp.domain

/**
 * GeoCodeUseCase for GeoCode
 */
import com.example.weatherapp.data.network.model.geocodemodel.GeoCodes
import com.example.weatherapp.data.repository.WeatherAppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class GeoCodeUseCase @Inject constructor(private val weatherAppRepository: WeatherAppRepository) {
    /**
     * getGeoCode for GeoCode
     */
    suspend  fun getGeoCode(queries: Map<String, String>): Flow<GeoCodes> {
      return  weatherAppRepository.getGeoCode(queries)
    }

}

