package com.example.weatherapp.presentation.uimodel

import com.example.weatherapp.data.network.model.geocodemodel.GeoCodesItem

data class GeoCodeUIModel(val isLoading: Boolean,val geoCodesItem: GeoCodesItem?)
