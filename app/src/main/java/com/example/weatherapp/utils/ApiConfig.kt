package com.example.weatherapp.utils

import com.example.weatherapp.utils.ApiConfig.API_KEY

/**
 * AppConfig utils
 */
/**
 * AppConfig utils for API configuration
 */
internal  object ApiConfig {
        const val BASE_URL = "https://api.openweathermap.org"
        const val API_KEY = "6b752f3d405f987d00a62dd42d7b4017"
        const val WEATHER_ENDPOINT = "/data/2.5/weather"
        const val WEATHER_ICON_ENDPOINT = "https://openweathermap.org/img/wn/"
        const val GEO_ENDPOINT = "/geo/1.0/direct"
}

/**
 * This method to form weather API query param
 */
fun getWeatherQuery(lat:String,lon:String):HashMap<String,String> {
    val query = HashMap<String, String>()
    query["lat"] = lat
    query["lon"] = lon
    query[ "appid"] = API_KEY
    query["units"] = "metric"
    return query
}

/**
 * This method to form geocode API query param
 */
fun getGeoCodeQuery(searchString:String):HashMap<String,String> {
    val query = HashMap<String, String>()
    query["q"] = searchString
    query[ "appid"] = API_KEY
    return query
}