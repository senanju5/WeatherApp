package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherUIDetails
import com.example.weatherapp.data.network.model.geocodemodel.GeoCodes
import com.example.weatherapp.data.network.model.geocodemodel.GeoCodesItem
import com.example.weatherapp.data.network.model.weathermodel.Weather
import com.example.weatherapp.data.network.model.weathermodel.WeatherModel
import com.example.weatherapp.domain.GeoCodeUseCase
import com.example.weatherapp.domain.WeatherUseCase
import com.example.weatherapp.presentation.uimodel.WeatherUIModel
import com.example.weatherapp.utils.ApiConfig
import com.example.weatherapp.utils.getWeatherQuery
import com.example.weatherapp.utils.getTimestampToDateTimeString
import com.example.weatherapp.utils.getTimestampToTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Weather view model for getting weather details
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val geoCodeUseCase: GeoCodeUseCase
) : ViewModel() {

    private val weatherMutableSate by lazy { MutableStateFlow(WeatherUIModel(false, null, false)) }
    internal val weatherUIModel: StateFlow<WeatherUIModel> = weatherMutableSate


    /**
     * Get geo code details
     */
    fun getGeoCode(queries: Map<String, String>) {
        weatherMutableSate.value = WeatherUIModel(true, null, false)
        viewModelScope.launch {
            geoCodeUseCase.getGeoCode(queries).collect {
                val geoCodeUIModel = handleGeoCodeResponse(it)
                if (geoCodeUIModel != null) {
                    getWeatherDetails(
                        getWeatherQuery(
                            geoCodeUIModel.lat.toString(),
                            geoCodeUIModel.lon.toString()
                        )
                    )
                } else {
                    weatherMutableSate.value = WeatherUIModel(false, null, true)
                }
            }
        }
    }

    /**
     * Handle geo code response
     */
    private fun handleGeoCodeResponse(geoCodes: GeoCodes): GeoCodesItem? {
        if (geoCodes.isNotEmpty()) {
            return geoCodes[0]
        } else {
            return null
        }
    }

    /**
     * Get weather details
     */
    fun getWeatherDetails(queries: Map<String, String>) {
        weatherMutableSate.value = WeatherUIModel(true, null, false)

        viewModelScope.launch {
            weatherUseCase.getWeatherDetails(queries).collect {
                val weatherDetails = handleWeatherResponse(it)
                if (weatherDetails != null) {
                    weatherMutableSate.value = WeatherUIModel(false, weatherDetails, false)
                } else {
                    weatherMutableSate.value = WeatherUIModel(false, null, true)
                }
            }
        }
    }

    /**
     * Handle weather response
     */
    private fun handleWeatherResponse(weatherDetails: WeatherModel): WeatherUIDetails? {
        val weather = getWeather(weatherDetails.weather)
        return WeatherUIDetails(
            dateTime = weatherDetails.dt.getTimestampToDateTimeString(),
            temperature = weatherDetails.main.temp.toString(),
            cityAndCountry = "${weatherDetails.name}, ${weatherDetails.sys.country}",
            weatherIcon = "${ApiConfig.WEATHER_ICON_ENDPOINT + weather.icon}.png",
            weatherIconDesc = weather.description,
            humidity = "${weatherDetails.main.humidity}%",
            pressure = "${weatherDetails.main.pressure} mBar",
            visibility = "${weatherDetails.visibility / 1000.0} KM",
            sunrise = weatherDetails.sys.sunrise.getTimestampToTimeString(),
            sunset = weatherDetails.sys.sunset.getTimestampToTimeString(),
            lat = weatherDetails.coord.lat.toString(),
            lon = weatherDetails.coord.lon.toString()
        )
    }

    /**
     * Get weather
     */
    private fun getWeather(weatherList: List<Weather>): Weather {
        if (weatherList.isNotEmpty()) {
            return Weather(
                description = weatherList[0].description,
                icon = weatherList[0].icon,
                id = weatherList[0].id,
                main = weatherList[0].main
            )
        } else {
            return Weather(
                description = "",
                icon = "",
                id = -1,
                main = ""
            )
        }

    }

}