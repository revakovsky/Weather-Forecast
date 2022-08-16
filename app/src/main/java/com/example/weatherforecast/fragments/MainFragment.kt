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
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.MyViewPagerAdapter
import com.example.weatherforecast.adapters.WeatherData
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val API_KEY = "864b68ca42224665a99104528220807"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val listOfFragments = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())

    private val tabIcons = listOf(
        R.drawable.ic_baseline_access_time,
        R.drawable.ic_baseline_today
    )

    private val viewModel = MainViewModel()

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
        updateCurrentWeatherView()
        requestWeatherData("Kiev")    //change city later to input field
    }

    private fun initTabs() = with(binding) {
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

    private fun getDaysForecastList(jsonObjects: JSONObject) : List<WeatherData> {
        val daysForecastList = arrayListOf<WeatherData>()
        val daysArrayFromJSONResult = jsonObjects.getJSONObject("forecast").getJSONArray("forecastday")
        val city = jsonObjects.getJSONObject("location").getString("name")

        for (i in 0 until daysArrayFromJSONResult.length()) {
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
        viewModel.liveDataCurrent.value = currentWeatherData
    }

    private fun updateCurrentWeatherView() = with(binding) {
        viewModel.liveDataCurrent.observe(viewLifecycleOwner) {
            cardMainDate.text = convertDate(it.date)
            cardMainCurrentTemperature.text = "${it.currentTemp}°C"
            cardMainCity.text = it.city
            cardMainDescription.text = it.weatherDescription
            cardMainMinTemp.text = "${it.minTemp}°"
            cardMainMaxTemp.text = "${it.maxTemp}°"
            cardMainFeelingTempValue.text = "${it.feelingTemp}°"
            Picasso.get().load("https:" + it.imageURL).into(cardMainWeatherIcon)
        }
    }

    private fun convertDate(date: String) : String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        val localDateTime = LocalDate.parse(date, formatter)
        val month = localDateTime.month.toString().lowercase().replaceFirstChar { it.uppercase() }
        val day = localDateTime.dayOfMonth
        val dayName = localDateTime.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }

        return "$dayName, $day of $month"
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