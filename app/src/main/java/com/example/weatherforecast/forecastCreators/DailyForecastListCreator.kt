package com.example.weatherforecast.forecastCreators

import com.example.weatherforecast.adapters.WeatherData
import org.json.JSONObject

class DailyForecastListCreator {

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
}