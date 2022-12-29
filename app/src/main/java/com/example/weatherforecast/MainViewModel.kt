package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.model.forecastCreators.DailyForecastCreator
import com.example.weatherforecast.model.forecastCreators.HourlyForecastCreator
import com.example.weatherforecast.model.forecastCreators.PresentForecastCreator
import com.example.weatherforecast.model.models.Response
import com.example.weatherforecast.model.models.WeatherData
import com.example.weatherforecast.network.ApiClient
import com.example.weatherforecast.utils.Constants
import retrofit2.Call
import retrofit2.Callback

class MainViewModel : ViewModel() {

    private val dailyForecastCreator = DailyForecastCreator()
    private val presentForecastCreator = PresentForecastCreator()
    private val hourlyForecastCreator = HourlyForecastCreator()

    private val _forecastAtPresent = MutableLiveData<WeatherData>()
    val forecastAtPresent: LiveData<WeatherData> = _forecastAtPresent

    private val _hourlyForecast = MutableLiveData<List<WeatherData>>()
    val hourlyForecast: LiveData<List<WeatherData>> = _hourlyForecast

    private val _dailyForecast = MutableLiveData<List<WeatherData>>()
    val dailyForecast: LiveData<List<WeatherData>> = _dailyForecast

    private var _toastCode = MutableLiveData<Int>()
    val toastCode: LiveData<Int> = _toastCode

    fun makeRequest(city: String) {
        val apiClient = ApiClient.createClient()

        val response = apiClient.getForecast(Constants.API_KEY, city)

        response.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val data: Response = response.body()!!

                    val dailyForecast: List<WeatherData> =
                        dailyForecastCreator.getDailyData(data)

                    val currentDayForecast = dailyForecast[0]
                    val tomorrowDayForecast = dailyForecast[1]

                    val forecastAtPresent: WeatherData =
                        presentForecastCreator.getForecastAtPresent(data)

                    val hourlyForecast: List<WeatherData> =
                        hourlyForecastCreator.getHourlyForecast(
                            currentDayForecast,
                            tomorrowDayForecast
                        )

                    _forecastAtPresent.value = forecastAtPresent
                    _hourlyForecast.value = hourlyForecast
                    _dailyForecast.value = dailyForecast

                } else {
                    _toastCode.value = AppCompatActivity.RESULT_OK
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _toastCode.value = AppCompatActivity.RESULT_CANCELED
            }
        })
    }
}