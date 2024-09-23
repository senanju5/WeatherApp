package com.example.weatherapp.data.network.model.weathermodel


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    val deg: Int, // 210
    @SerializedName("gust")
    val gust: Double, // 0.5
    @SerializedName("speed")
    val speed: Double // 1.26
)