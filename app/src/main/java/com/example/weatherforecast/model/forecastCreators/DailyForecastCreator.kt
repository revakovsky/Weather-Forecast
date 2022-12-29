package com.example.weatherforecast.model.forecastCreators

import com.example.weatherforecast.model.models.Response
import com.example.weatherforecast.model.models.WeatherData

class DailyForecastCreator {

    fun getDailyData(data: Response): List<WeatherData> {
        val dailyForecastData = arrayListOf<WeatherData>()
        val dailyData = data.forecast.forecastday

        for (oneDayData in dailyData) {
            dailyForecastData.add(
                WeatherData(
                    city = data.location.name,
                    date = oneDayData.date,
                    weatherDescription = oneDayData.day.condition.text,
                    imageURL = oneDayData.day.condition.icon,
                    currentTemp = data.current.temp_c.toInt().toString(),
                    minTemp = oneDayData.day.mintemp_c.toInt().toString(),
                    maxTemp = oneDayData.day.maxtemp_c.toInt().toString(),
                    feelingTemp = data.current.feelslike_c.toInt().toString(),
                    chanceOfRain = oneDayData.day.daily_chance_of_rain.toString(),
                    hourlyForecast = oneDayData.hour
                )
            )
        }
        return dailyForecastData
    }
}
