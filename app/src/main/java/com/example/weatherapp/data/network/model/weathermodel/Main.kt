package com.example.weatherapp.data.network.model.weathermodel


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double, // 284.86
    @SerializedName("grnd_level")
    val grndLevel: Int, // 954
    @SerializedName("humidity")
    val humidity: Int, // 94
    @SerializedName("pressure")
    val pressure: Int, // 1021
    @SerializedName("sea_level")
    val seaLevel: Int, // 1021
    @SerializedName("temp")
    val temp: Double, // 285.15
    @SerializedName("temp_max")
    val tempMax: Double, // 285.18
    @SerializedName("temp_min")
    val tempMin: Double // 284.41
)