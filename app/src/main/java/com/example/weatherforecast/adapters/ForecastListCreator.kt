package com.example.weatherforecast.adapters

import org.json.JSONArray
import org.json.JSONObject

class ForecastListCreator {
    private val timeConverter = DateTimeConverter()
    private val hourlyForecastList = ArrayList<WeatherData>()

    fun getDailyWeatherDataList(jsonObjects: JSONObject): List<WeatherData> {
        val dailyWeatherDataList = arrayListOf<WeatherData>()
        val dailyWeatherDataListFromJSONResponse = jsonObjects.getJSONObject("forecast").getJSONArray("forecastday")
        val cityName = jsonObjects.getJSONObject("location").getString("name")

        for (i in 0 until dailyWeatherDataListFromJSONResponse.length()) {
            val oneDayWeatherData = dailyWeatherDataListFromJSONResponse[i] as JSONObject
            val forecastPerOneDay = WeatherData(
                cityName,
                oneDayWeatherData.getString("date"),
                oneDayWeatherData.getJSONObject("day").getJSONObject("condition").getString("text"),
                oneDayWeatherData.getJSONObject("day").getJSONObject("condition").getString("icon"),
                "",
                oneDayWeatherData.getJSONObject("day").getString("mintemp_c"),
                oneDayWeatherData.getJSONObject("day").getString("maxtemp_c"),
                "",
                oneDayWeatherData.getJSONObject("day").getString("daily_chance_of_rain"),
                oneDayWeatherData.getJSONArray("hour").toString()
            )
            dailyWeatherDataList.add(forecastPerOneDay)
        }
        return dailyWeatherDataList
    }


    fun getHourlyForecastList(forecastDataAtPresent: WeatherData,
                              tomorrowDayForecast: WeatherData): List<WeatherData> {

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

    fun getForecastDataAtPresent(jsonObjects: JSONObject, currentDayForecast: WeatherData) : WeatherData {
        return WeatherData(
            jsonObjects.getJSONObject("location").getString("name"),
            jsonObjects.getJSONObject("current").getString("last_updated"),
            jsonObjects.getJSONObject("current").getJSONObject("condition").getString("text"),
            jsonObjects.getJSONObject("current").getJSONObject("condition").getString("icon"),
            jsonObjects.getJSONObject("current").getString("temp_c"),
            currentDayForecast.minTemp,
            currentDayForecast.maxTemp,
            jsonObjects.getJSONObject("current").getString("feelslike_c"),
            currentDayForecast.chanceOfRain,
            currentDayForecast.hourlyForecast
        )
    }

    private fun convertTemperature(temp : String) : String {
        val tempToDouble = temp.toDouble()
        return String.format("%.1f", tempToDouble)
    }

}