package com.example.weatherforecast.model.forecastCreators

import com.example.weatherforecast.model.models.Response
import com.example.weatherforecast.model.models.WeatherData
import com.example.weatherforecast.utils.DateTimeConverter
import kotlin.math.roundToInt

class DailyForecastCreator {

    private val timeConverter = DateTimeConverter()

    fun getDailyData(data: Response): List<WeatherData> {
        val dailyForecastData = arrayListOf<WeatherData>()
        val dailyData = data.forecast.forecastday

        for (oneDayData in dailyData) {
            dailyForecastData.add(
                WeatherData(
                    city = "",
                    date = timeConverter.convertDate(oneDayData.date, 2),
                    weatherDescription = oneDayData.day.condition.text,
                    imageURL = oneDayData.day.condition.icon,
                    currentTemp = "",
                    minTemp = oneDayData.day.mintemp_c.roundToInt().toString(),
                    maxTemp = oneDayData.day.maxtemp_c.roundToInt().toString(),
                    feelingTemp = data.current.feelslike_c.roundToInt().toString(),
                    chanceOfRain = oneDayData.day.daily_chance_of_rain.toString(),
                    hourlyForecast = oneDayData.hour
                )
            )
        }
        return dailyForecastData
    }
}
