package com.example.weatherforecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.adapters.WeatherData

class MainViewModel : ViewModel() {
    val currentDayForecastLiveData = MutableLiveData<WeatherData> ()
    val hourlyForecastLiveData = MutableLiveData<ArrayList<WeatherData>>()

    fun refreshForecastLiveData(forecastDataAtPresent: WeatherData,
                                tomorrowDayForecast: WeatherData) {

        currentDayForecastLiveData.value = forecastDataAtPresent
        hourlyForecastLiveData.value = arrayListOf(forecastDataAtPresent, tomorrowDayForecast)
    }
}