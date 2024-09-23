package com.example.weatherapp.data.network.model.weathermodel


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    val country: String, // IT
    @SerializedName("id")
    val id: Int, // 2004688
    @SerializedName("sunrise")
    val sunrise: Int, // 1726894936
    @SerializedName("sunset")
    val sunset: Int, // 1726938951
    @SerializedName("type")
    val type: Int // 2
)