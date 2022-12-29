package com.example.weatherforecast.model.forecastCreators

import com.example.weatherforecast.model.models.Hour
import com.example.weatherforecast.model.models.Response
import com.example.weatherforecast.model.models.WeatherData
import com.example.weatherforecast.utils.DateTimeConverter

class PresentForecastCreator {

    private val timeConverter = DateTimeConverter()
    private val currentTime = timeConverter.getCurrentTime()
    private lateinit var dataForCurrentTime: Hour

    fun getForecastAtPresent(
        response: Response,
        currentDayForecast: WeatherData
    ): WeatherData {

        val hourlyData = currentDayForecast.hourlyForecast

        dataForCurrentTime = if (currentTime == 0) hourlyData!![currentTime]
        else hourlyData!![currentTime - 1]

        return WeatherData(
            city = response.location.name,
            date = response.current.last_updated,
            weatherDescription = dataForCurrentTime.condition.text,
            imageURL = dataForCurrentTime.condition.icon,
            currentTemp = dataForCurrentTime.temp_c.toInt().toString(),
            minTemp = currentDayForecast.minTemp,
            maxTemp = currentDayForecast.maxTemp,
            feelingTemp = dataForCurrentTime.feelslike_c.toInt().toString(),
            chanceOfRain = dataForCurrentTime.chance_of_rain.toString(),
            hourlyForecast = hourlyData
        )
    }
}
