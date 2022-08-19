package com.example.weatherforecast.adapters

import org.json.JSONArray
import org.json.JSONObject

class ForecastListCreator {
    private val timeConverter = DateTimeConverter()

    fun getDailyForecastList(jsonObjects: JSONObject): List<WeatherData> {
        val dailyForecastList = arrayListOf<WeatherData>()
        val dailyForecastListFromJSONResponse = jsonObjects.getJSONObject("forecast").getJSONArray("forecastday")
        val cityName = jsonObjects.getJSONObject("location").getString("name")

        for (i in 0 until dailyForecastListFromJSONResponse.length()) {
            val oneDayForecastData = dailyForecastListFromJSONResponse[i] as JSONObject
            val forecastPerOneDay = WeatherData(
                cityName,
                oneDayForecastData.getString("date"),
                oneDayForecastData.getJSONObject("day").getJSONObject("condition").getString("text"),
                oneDayForecastData.getJSONObject("day").getJSONObject("condition").getString("icon"),
                "",
                oneDayForecastData.getJSONObject("day").getString("mintemp_c"),
                oneDayForecastData.getJSONObject("day").getString("maxtemp_c"),
                "",
                oneDayForecastData.getJSONObject("day").getString("daily_chance_of_rain"),
                oneDayForecastData.getJSONArray("hour").toString()
            )
            dailyForecastList.add(forecastPerOneDay)
        }
        return dailyForecastList
    }


    fun getHourlyForecastList(currentDayWeatherData: WeatherData, tomorrowDayWeatherData: WeatherData): List<WeatherData> {
        val hourlyForecastList = ArrayList<WeatherData>()

        val hourlyForecastForTodayFromJSONResponse = JSONArray(currentDayWeatherData.hourlyForecast)
        val hourlyForecastForTomorrowFromJSONResponse = JSONArray(tomorrowDayWeatherData.hourlyForecast)

        for (i in 0 until hourlyForecastForTodayFromJSONResponse.length()) {
            val oneHourDataForToday = hourlyForecastForTodayFromJSONResponse[i] as JSONObject
            val oneHourDataForTomorrow = hourlyForecastForTodayFromJSONResponse[i] as JSONObject
            val forecastPerOneHour = WeatherData(
                "",
                timeConverter.convertTime(oneHourDataForToday.getString("time")),
                oneHourDataForToday.getJSONObject("condition").getString("text"),
                oneHourDataForToday.getJSONObject("condition").getString("icon"),
                oneHourDataForToday.getString("temp_c"),
                "", "", "",
                oneHourDataForToday.getString("chance_of_rain"),
                ""
            )
            hourlyForecastList.add(forecastPerOneHour)
        }
        return hourlyForecastList
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

}