package com.example.weatherforecast.forecastCreators

import com.example.weatherforecast.utils.DateTimeConverter
import com.example.weatherforecast.model.WeatherData
import org.json.JSONArray
import org.json.JSONObject

class HourlyForecastCreator {
    private val timeConverter = DateTimeConverter()
    private val hourlyForecastList = ArrayList<WeatherData>()

    fun getHourlyForecastList(forecastDataAtPresent: WeatherData,
                              tomorrowDayForecast: WeatherData
    ): List<WeatherData> {

        val hourlyDataFromJSONResponseForToday = JSONArray(forecastDataAtPresent.hourlyForecast)
        val hourlyDataFromJSONResponseForTomorrow = JSONArray(tomorrowDayForecast.hourlyForecast)

        val currentHour = timeConverter.getCurrentHour() + 1

        for (i in currentHour until hourlyDataFromJSONResponseForToday.length()) {
            fillInHourlyForecastList(hourlyDataFromJSONResponseForToday, i)
        }

        for (i in 0 until currentHour) {
            fillInHourlyForecastList(hourlyDataFromJSONResponseForTomorrow, i)
        }
        return hourlyForecastList
    }

    private fun fillInHourlyForecastList(hourlyDataFromJSONResponse: JSONArray, i : Int)  {
        val oneHourWeatherData = hourlyDataFromJSONResponse[i] as JSONObject

        val forecastPerOneHour = WeatherData(
            "",
            timeConverter.convertTime(oneHourWeatherData.getString("time")),
            oneHourWeatherData.getJSONObject("condition").getString("text"),
            oneHourWeatherData.getJSONObject("condition").getString("icon"),
            convertTemperature(oneHourWeatherData.getString("temp_c")),
            "", "", "",
            oneHourWeatherData.getString("chance_of_rain"),
            ""
        )
        hourlyForecastList.add(forecastPerOneHour)
    }

    private fun convertTemperature(temp : String) : String {
        val tempToDouble = temp.toDouble()
        return String.format("%.1f", tempToDouble)
    }
}