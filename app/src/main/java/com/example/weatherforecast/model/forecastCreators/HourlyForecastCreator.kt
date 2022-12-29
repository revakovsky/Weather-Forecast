package com.example.weatherforecast.model.forecastCreators

import com.example.weatherforecast.model.models.Hour
import com.example.weatherforecast.model.models.WeatherData
import com.example.weatherforecast.utils.DateTimeConverter
import kotlin.math.roundToInt

class HourlyForecastCreator {

    private val timeConverter = DateTimeConverter()
    private val hourlyForecast = ArrayList<WeatherData>()

    fun getHourlyForecast(
        currentDayForecast: WeatherData,
        tomorrowDayForecast: WeatherData
    ): List<WeatherData> {

        val hourlyDataForToday = currentDayForecast.hourlyForecast
        val hourlyDataForTomorrow = tomorrowDayForecast.hourlyForecast

        val currentTime = timeConverter.getCurrentTime() + 1

        for (i in currentTime until hourlyDataForToday!!.size) {
            val oneHourData = hourlyDataForToday[i]
            fillHourlyForecast(oneHourData)
        }

        for (i in 0 until currentTime) {
            val oneHourData = hourlyDataForTomorrow!![i]
            fillHourlyForecast(oneHourData)
        }
        return hourlyForecast
    }

    private fun fillHourlyForecast(oneHourData: Hour) {
        hourlyForecast.add(
            WeatherData(
                city = "",
                date = timeConverter.convertTime(oneHourData.time),
                weatherDescription = oneHourData.condition.text,
                imageURL = oneHourData.condition.icon,
                currentTemp = oneHourData.temp_c.roundToInt().toString(),
                minTemp = "",
                maxTemp = "",
                feelingTemp = "",
                chanceOfRain = oneHourData.chance_of_rain.toString(),
                hourlyForecast = null
            )
        )
    }
}
