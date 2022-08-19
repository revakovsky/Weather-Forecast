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
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.Constants
import com.example.weatherforecast.adapters.DateTimeConverter
import com.example.weatherforecast.adapters.ForecastListCreator
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.MyViewPagerAdapter
import com.example.weatherforecast.adapters.WeatherData
import com.example.weatherforecast.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val dateTimeConverter = DateTimeConverter()
    private val forecastListCreator = ForecastListCreator()
    private val viewModel : MainViewModel by activityViewModels()

    private val listOfFragments = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())

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
        requestForUpdateWeatherData("Kharkiv")    //change city later to input field
        showForecastDataAtPresent()
    }

    private fun requestForUpdateWeatherData(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                Constants.API_KEY +
                "&q=" +
                city +
                "&days=" +
                "5" +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseJSONData(result)
            },
            { error ->
                Log.d("testLogs", "error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseJSONData(result: String) {
        val jsonObjects = JSONObject(result)
        val dailyForecastList: List<WeatherData> = forecastListCreator.getDailyForecastList(jsonObjects)
        val currentDayForecast = dailyForecastList[0]
        val forecastDataAtPresent = forecastListCreator.getForecastDataAtPresent(jsonObjects, currentDayForecast)
        viewModel.refreshHourlyForecastLiveData(forecastDataAtPresent)
    }

    private fun showForecastDataAtPresent() = with(binding) {
        viewModel.hourlyForecastLiveData.observe(viewLifecycleOwner) {
            cardMainDate.text = dateTimeConverter.convertDate(it.date)
            cardMainCurrentTemperature.text = "${it.currentTemp}°C"
            cardMainCity.text = it.city
            cardMainDescription.text = it.weatherDescription
            cardMainMinTemp.text = "${it.minTemp}°"
            cardMainMaxTemp.text = "${it.maxTemp}°"
            cardMainFeelingTempValue.text = "${it.feelingTemp}°"
            Picasso.get().load("https:" + it.imageURL).into(cardMainWeatherIcon)
        }
    }


    private fun initTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
        }.attach()
    }


    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    private fun permissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}