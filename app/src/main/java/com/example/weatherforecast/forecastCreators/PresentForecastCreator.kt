package com.example.weatherforecast.forecastCreators

import com.example.weatherforecast.utils.DateTimeConverter
import com.example.weatherforecast.model.WeatherData
import org.json.JSONArray
import org.json.JSONObject

class PresentForecastCreator {
    private val timeConverter = DateTimeConverter()

    fun getForecastDataAtPresent(jsonObjects: JSONObject, currentDayForecast: WeatherData) : WeatherData {
        val hourlyDataListFromJSON = JSONArray(currentDayForecast.hourlyForecast)
        val currentHour = timeConverter.getCurrentHour()
        val currentHourData = hourlyDataListFromJSON[currentHour-1] as JSONObject

        return WeatherData(
            jsonObjects.getJSONObject("location").getString("name"),
            jsonObjects.getJSONObject("current").getString("last_updated"),
            currentHourData.getJSONObject("condition").getString("text"),
            currentHourData.getJSONObject("condition").getString("icon"),
            currentHourData.getString("temp_c"),
            currentDayForecast.minTemp,
            currentDayForecast.maxTemp,
            currentHourData.getString("feelslike_c"),
            currentHourData.getString("chance_of_rain"),
            currentDayForecast.hourlyForecast
        )
    }
}