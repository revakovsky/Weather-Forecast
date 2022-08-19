package com.example.weatherforecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.adapters.WeatherData

class MainViewModel : ViewModel() {
    val currentDayForecastLiveData = MutableLiveData<WeatherData> ()
    val hourlyForecastLiveData = MutableLiveData<WeatherData>()

    fun refreshHourlyForecastLiveData(currentDayWeatherData: WeatherData) {
        hourlyForecastLiveData.value = currentDayWeatherData
    }
}