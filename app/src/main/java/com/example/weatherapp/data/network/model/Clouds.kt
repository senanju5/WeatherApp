package com.example.weatherapp.data.network.model


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int // 19
)