package com.example.weatherapp.data.network.model.weathermodel


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("description")
    val description: String, // few clouds
    @SerializedName("icon")
    val icon: String, // 02n
    @SerializedName("id")
    val id: Int, // 801
    @SerializedName("main")
    val main: String // Clouds
)