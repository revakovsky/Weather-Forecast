package com.example.weatherforecast.model.models

data class Response(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)