package com.example.weatherforecast.model.models

data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val wind_kph: Double,
    val wind_mph: Double
)