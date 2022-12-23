package com.example.weatherforecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.model.WeatherData

class MainViewModel : ViewModel() {

    val currentDayForecast = MutableLiveData<WeatherData>()

    val hourlyForecast = MutableLiveData<ArrayList<WeatherData>>()

    fun refreshForecast(
        forecastDataAtPresent: WeatherData,
        tomorrowDayForecast: WeatherData
    ) {
        currentDayForecast.value = forecastDataAtPresent
        hourlyForecast.value = arrayListOf(forecastDataAtPresent, tomorrowDayForecast)
    }
}