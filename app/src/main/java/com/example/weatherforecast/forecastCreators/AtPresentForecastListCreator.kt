package com.example.weatherforecast.forecastCreators

import com.example.weatherforecast.adapters.WeatherData
import org.json.JSONObject

class AtPresentForecastListCreator {
    
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