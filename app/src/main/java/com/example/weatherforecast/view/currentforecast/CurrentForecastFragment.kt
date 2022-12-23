package com.example.weatherforecast.view.currentforecast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.Constants
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.utils.DateTimeConverter
import com.example.weatherforecast.R
import com.example.weatherforecast.forecastCreators.PresentForecastCreator
import com.example.weatherforecast.utils.adapters.MyViewPagerAdapter
import com.example.weatherforecast.model.WeatherData
import com.example.weatherforecast.contract.MainContract
import com.example.weatherforecast.databinding.FragmentCurrentForecastBinding
import com.example.weatherforecast.forecastCreators.DailyForecastCreator
import com.example.weatherforecast.view.dailyforecast.DailyForecastFragment
import com.example.weatherforecast.view.hourslyforecast.HourlyForecastFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

  class CurrentForecastFragment : Fragment(R.layout.fragment_current_forecast), MainContract.View {

    private val presenter = CurrentForecastPresenter(this)

    private lateinit var binding: FragmentCurrentForecastBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val dateTimeConverter = DateTimeConverter()
    private val dailyForecastCreator = DailyForecastCreator()
    private val presentForecastCreator = PresentForecastCreator()
    private val viewModel: MainViewModel by activityViewModels()

    private val listOfFragments =
        listOf(HourlyForecastFragment.newInstance(), DailyForecastFragment.newInstance())

    private val tabIcons = listOf(
        R.drawable.ic_baseline_access_time,
        R.drawable.ic_baseline_today
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentForecastBinding.bind(view)

        runPermissionLauncher()
        presenter.startPermissionLauncher()
        showTabs()
        requestForUpdateWeatherData("Kharkiv")    //change city later to input field
        //presenter.getCurrentForecast(activity as LifecycleOwner)
        showCurrentForecast()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetachView()
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
        val dailyWeatherDataList: List<WeatherData> =
            dailyForecastCreator.getDailyWeatherDataList(jsonObjects)

        val currentDayForecast = dailyWeatherDataList[0]
        val tomorrowDayForecast = dailyWeatherDataList[1]
        val forecastDataAtPresent: WeatherData =
            presentForecastCreator.getForecastDataAtPresent(jsonObjects, currentDayForecast)

        viewModel.refreshForecast(forecastDataAtPresent, tomorrowDayForecast)
    }

//    override fun showCurrentForecast(weatherData: WeatherData) = with(binding) {
//        cardMainDate.text = dateTimeConverter.convertDate(weatherData.date)
//        cardMainCurrentTemperature.text = "${weatherData.currentTemp}°C"
//        cardMainCity.text = weatherData.city
//        cardMainDescription.text = weatherData.weatherDescription
//        cardMainMinTemp.text = "${weatherData.minTemp}°"
//        cardMainMaxTemp.text = "${weatherData.maxTemp}°"
//        cardMainFeelingTempValue.text = "${weatherData.feelingTemp}°"
//        Picasso.get().load("https:" + weatherData.imageURL).into(cardMainWeatherIcon)
//    }


    //override
    fun showCurrentForecast() = with(binding) {
        viewModel.currentDayForecast.observe(viewLifecycleOwner) {
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

    private fun showTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
        }.attach()
    }

      // todo
    private fun runPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                presenter.onPermissionListenerCallback(it)
            }
    }

    override fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity as AppCompatActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun showPermission() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun showPermissionGranted() {
        Toast.makeText(activity, getString(R.string.permission_is_granted), Toast.LENGTH_LONG)
            .show()
    }

    override fun showPermissionDenied() {
        Toast.makeText(activity, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
    }

}