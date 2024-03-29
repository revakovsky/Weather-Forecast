package com.example.weatherforecast.model.models

data class WeatherData(
    val city: String? = "",
    val date: String,
    val weatherDescription: String,
    val imageURL: String,
    val currentTemp: String,
    val minTemp: String,
    val maxTemp: String,
    val feelingTemp: String,
    val chanceOfRain: String? = "",
    val hourlyForecast: List<Hour>? = null
)
