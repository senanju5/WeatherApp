package com.example.weatherapp.data.network.model


import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lat")
    val lat: Double, // 44.34
    @SerializedName("lon")
    val lon: Double // 10.99
)