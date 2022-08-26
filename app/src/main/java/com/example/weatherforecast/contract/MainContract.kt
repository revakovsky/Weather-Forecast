package com.example.weatherforecast.contract

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.example.weatherforecast.adapters.WeatherData

interface MainContract {

    interface Model {

    }

    interface View : LifecycleOwner {
        fun showPermission()
        fun isPermissionGranted(): Boolean
        fun showPermissionGranted()
        fun showPermissionDenied()
        //fun showCurrentForecast(weatherData: WeatherData)
    }

    interface Presenter {
        fun onDetachView()
        fun startPermissionLauncher()
        fun onPermissionListenerCallback(permissionLauncher: Boolean)
        //fun getCurrentForecast(lifecycleOwner : LifecycleOwner)
    }

}