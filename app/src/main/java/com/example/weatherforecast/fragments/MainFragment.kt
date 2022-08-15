package com.example.weatherforecast.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.MyViewPagerAdapter
import com.example.weatherforecast.adapters.WeatherData
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

const val API_KEY = "864b68ca42224665a99104528220807"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val listOfFragments = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())
    private val listOfTabsNames = listOf("hours", "days")

    private val tabIcons = listOf(
        R.drawable.ic_baseline_access_time,
        R.drawable.ic_baseline_today
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initTabs()
        requestWeatherData("London")    //change city later to input field
    }

    fun initTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            //tab.text = listOfTabsNames[position]
            tab.setIcon(tabIcons[position])

        }.attach()
    }

    private fun requestWeatherData(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "5" +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result ->
                parseJSONData(result)
            },
            {
                error ->
                Log.d("testLogs", "error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseJSONData(result: String) {
        val jsonObjects = JSONObject(result)
        val daysForecastList : List<WeatherData> = getDaysForecastList(jsonObjects)
        getCurrentWeatherData(jsonObjects, daysForecastList[0])
    }

    fun getDaysForecastList(jsonObjects: JSONObject) : List<WeatherData> {
        val daysForecastList = arrayListOf<WeatherData>()
        val daysArrayFromJSONResult = jsonObjects.getJSONObject("forecast").getJSONArray("forecastday")
        val city = jsonObjects.getJSONObject("location").getString("name")

        for (i in 0..daysArrayFromJSONResult.length()) {
            val day = daysArrayFromJSONResult[i] as JSONObject
            val forecastPerOneDay = WeatherData(
                city,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                "",
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getString("maxtemp_c"),
                "",
                day.getJSONObject("day").getString("daily_chance_of_rain")
            )
            daysForecastList.add(forecastPerOneDay)
        }
        return daysForecastList
    }

    private fun getCurrentWeatherData(jsonObjects: JSONObject, currentDayForecast: WeatherData) {
        val currentWeatherData = WeatherData(
            jsonObjects.getJSONObject("location").getString("name"),
            jsonObjects.getJSONObject("current").getString("last_updated"),
            jsonObjects.getJSONObject("current").getJSONObject("condition").getString("text"),
            jsonObjects.getJSONObject("current").getJSONObject("condition").getString("icon"),
            jsonObjects.getJSONObject("current").getString("temp_c"),
            currentDayForecast.minTemp,
            currentDayForecast.maxTemp,
            jsonObjects.getJSONObject("current").getString("feelslike_c"),
            currentDayForecast.chanceOfRain,
        )
        Log.d("testLogs", "Item info: ${currentWeatherData.city}, ${currentWeatherData.currentTemp}, ${currentWeatherData.weatherDescription}")
    }

    private fun permissionListener() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}