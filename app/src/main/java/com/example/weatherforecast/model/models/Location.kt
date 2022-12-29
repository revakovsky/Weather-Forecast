package com.example.weatherforecast.model.models

data class Location(
    val localtime: String,
    val lon: Double,
    val name: String,
    val region: String,
    val tz_id: String
)