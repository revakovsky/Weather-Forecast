package com.example.weatherforecast.model.forecastCreators

import com.example.weatherforecast.model.models.Response
import com.example.weatherforecast.model.models.WeatherData
import kotlin.math.roundToInt

class PresentForecastCreator {

    fun getForecastAtPresent(response: Response): WeatherData {
        return WeatherData(
            city = response.location.name,
            date = response.current.last_updated,
            weatherDescription = response.current.condition.text,
            imageURL = response.current.condition.icon,
            currentTemp = response.current.temp_c.roundToInt().toString(),
            minTemp = response.forecast.forecastday[0].day.mintemp_c.roundToInt().toString(),
            maxTemp = response.forecast.forecastday[0].day.maxtemp_c.roundToInt().toString(),
            feelingTemp = response.current.feelslike_c.roundToInt().toString(),
            chanceOfRain = "",
            hourlyForecast = null
        )
    }
}
