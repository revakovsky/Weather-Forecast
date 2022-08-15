package com.example.weatherforecast.adapters

data class WeatherData(
    val city: String,
    val time: String,
    val weatherDescription: String,
    val imageURL: String,
    val currentTemp: String,
    val minTemp: String,
    val maxTemp: String,
    val feelingTemp: String,
    val chanceOfRain: String,
)
