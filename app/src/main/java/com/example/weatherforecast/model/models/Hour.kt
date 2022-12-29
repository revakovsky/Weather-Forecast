package com.example.weatherforecast.model.models

data class Hour(
    val chance_of_rain: Int,
    val chance_of_snow: Int,
    val cloud: Int,
    val condition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val humidity: Int,
    val temp_c: Double,
    val temp_f: Double,
    val time: String,
    val uv: Double,
    val will_it_rain: Int,
    val will_it_snow: Int,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_kph: Double,
    val wind_mph: Double
)