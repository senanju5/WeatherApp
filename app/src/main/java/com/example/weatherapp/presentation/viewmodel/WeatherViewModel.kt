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
import com.example.weatherapp.presentation.uimodel.GeoCodeUIModel
import com.example.weatherapp.presentation.uimodel.WeatherUIModel
import com.example.weatherapp.utils.ApiConfig
import com.example.weatherapp.utils.getWeatherQuery
import com.example.weatherapp.utils.unixTimestampToDateTimeString
import com.example.weatherapp.utils.unixTimestampToTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase, private val geoCodeUseCase: GeoCodeUseCase) : ViewModel(){

    private val geoCodeMutableSate by lazy { MutableStateFlow(GeoCodeUIModel(true, null))  }
    internal val geoCodeUIModel : StateFlow<GeoCodeUIModel> = geoCodeMutableSate

    private val weatherMutableSate by lazy { MutableStateFlow(WeatherUIModel(true, null))  }
    internal val weatherUIModel : StateFlow<WeatherUIModel> = weatherMutableSate


     fun getGeoCode(queries: Map<String, String>) {
        viewModelScope.launch {
            geoCodeUseCase.getGeoCode(queries).collect{
                val geoCodeUIModel =handleGeoCodeResponse(it)
                if(geoCodeUIModel != null){
                    getWeatherDetails(getWeatherQuery(geoCodeUIModel.lat, geoCodeUIModel.lon))
                } else {
                    geoCodeMutableSate.value = GeoCodeUIModel(false, null)
                }

            }
        }
    }

    private fun handleGeoCodeResponse(geoCodes: GeoCodes): GeoCodesItem? {
        if(geoCodes.isNotEmpty()){
            return geoCodes[0]
        } else {
            return null
        }
    }

    fun getWeatherDetails(queries: Map<String, String>) {
        viewModelScope.launch {
            weatherUseCase.getWeatherDetails(queries).collect{
                val weatherDetails =handleWeatherResponse(it)
                weatherMutableSate.value = WeatherUIModel(false, weatherDetails)

            }
    }
     }

    private fun handleWeatherResponse(weatherDetails: WeatherModel): WeatherUIDetails {
        val weather = getWeather(weatherDetails.weather)
       return WeatherUIDetails(
            dateTime = weatherDetails.dt.unixTimestampToDateTimeString(),
            temperature = weatherDetails.main.temp.toString(),
            cityAndCountry = "${weatherDetails.name}, ${weatherDetails.sys.country}",
            weatherIcon = "${ApiConfig.WEATHER_ICON_ENDPOINT+weather.icon}.png",
            weatherIconDesc = weather.description,
            humidity = "${weatherDetails.main.humidity}%",
            pressure = "${weatherDetails.main.pressure} mBar",
            visibility = "${weatherDetails.visibility/1000.0} KM",
            sunrise = weatherDetails.sys.sunrise.unixTimestampToTimeString(),
            sunset = weatherDetails.sys.sunset.unixTimestampToTimeString())
    }

    fun getWeather(weatherList: List<Weather>): Weather {
        if(weatherList.isNotEmpty()){
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