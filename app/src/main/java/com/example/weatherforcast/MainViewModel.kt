package com.example.weatherforcast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforcast.adapters.WeatherData

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherData>()
    val liveDataList = MutableLiveData<WeatherData>()
}