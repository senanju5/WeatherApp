package com.example.weatherapp.data.network.model.geocodemodel


import com.google.gson.annotations.SerializedName

data class GeoCodesItem(
    @SerializedName("country")
    val country: String, // US
    @SerializedName("lat")
    val lat: Double, // 42.0811563
    @SerializedName("lon")
    val lon: Double, // -87.9802164
    @SerializedName("name")
    val name: String, // Arlington Heights
    @SerializedName("state")
    val state: String // Illinois
)