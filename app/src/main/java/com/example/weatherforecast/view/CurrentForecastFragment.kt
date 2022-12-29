package com.example.weatherforecast.view

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentCurrentForecastBinding
import com.example.weatherforecast.utils.DateTimeConverter
import com.example.weatherforecast.utils.adapters.MyViewPagerAdapter
import com.example.weatherforecast.utils.isPermissionProhibited
import com.example.weatherforecast.utils.showToast
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class CurrentForecastFragment : Fragment(R.layout.fragment_current_forecast) {

    private lateinit var binding: FragmentCurrentForecastBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val viewModel: MainViewModel by activityViewModels()

    private val dateTimeConverter = DateTimeConverter()

    private val listOfFragments = listOf(
        HourlyForecastFragment::class.java.newInstance(),
        DailyForecastFragment::class.java.newInstance()
    )

    private val tabIcons = listOf(
        R.drawable.ic_baseline_access_time, R.drawable.ic_baseline_today
    )

    private val city = "Kharkiv"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentForecastBinding.bind(view)

        checkPermissions()
        showTabs()

        viewModel.makeRequest(city)

        initToastObserver()
        showCurrentForecast()
    }

    private fun checkPermissions() {
        if (isPermissionProhibited(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                    if (result) showToast(getString(R.string.permission_is_granted))
                    else showToast(getString(R.string.permission_denied))
                }
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
        }.attach()
    }

    private fun initToastObserver() {
        viewModel.toastCode.observe(viewLifecycleOwner) { code ->
            when (code) {
                AppCompatActivity.RESULT_OK -> showToast(getString(R.string.something_went_wrong))
                AppCompatActivity.RESULT_CANCELED -> showToast(getString(R.string.something_wrong_with_server))
            }
        }
    }

    private fun showCurrentForecast() = with(binding) {
        viewModel.currentDayForecast.observe(viewLifecycleOwner) { forecast ->
            cardMainDate.text = dateTimeConverter.convertDate(forecast.date)
            cardMainCurrentTemperature.text = "${forecast.currentTemp}°C"
            cardMainCity.text = forecast.city
            cardMainDescription.text = forecast.weatherDescription
            cardMainMinTemp.text = "${forecast.minTemp}°"
            cardMainMaxTemp.text = "${forecast.maxTemp}°"
            cardMainFeelingTempValue.text = "${forecast.feelingTemp}°"
            Picasso.get().load("https:" + forecast.imageURL).into(cardMainWeatherIcon)
        }
    }
}