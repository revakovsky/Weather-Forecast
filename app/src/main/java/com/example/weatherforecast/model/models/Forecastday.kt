package com.example.weatherforecast.model.models

data class Forecastday(
    val date: String,
    val day: Day,
    val hour: List<Hour>
)