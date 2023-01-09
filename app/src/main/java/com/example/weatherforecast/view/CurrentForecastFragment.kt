package com.example.weatherforecast.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentCurrentForecastBinding
import com.example.weatherforecast.utils.DateTimeConverter
import com.example.weatherforecast.utils.adapters.MyViewPagerAdapter
import com.example.weatherforecast.utils.isPermissionProhibited
import com.example.weatherforecast.utils.showLocationDialog
import com.example.weatherforecast.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class CurrentForecastFragment : Fragment(R.layout.fragment_current_forecast) {

    private lateinit var binding: FragmentCurrentForecastBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationClient: FusedLocationProviderClient
    private val viewModel: MainViewModel by activityViewModels()

    private val dateTimeConverter = DateTimeConverter()

    private val listOfFragments = listOf(
        HourlyForecastFragment(),
        DailyForecastFragment()
    )

    private val tabIcons = listOf(
        R.drawable.ic_baseline_access_time,
        R.drawable.ic_baseline_today
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentForecastBinding.bind(view)

        checkPermissions()
        showTabs()
        initViews()

        // todo create normal launch process

        checkResponseForError()
        showCurrentForecast()
    }

    private fun initViews() {
        binding.refresh.setOnClickListener {
            binding.apply { tabs.selectTab(tabs.getTabAt(0)) }
            checkLocation()
        }
    }

    private fun getLocation() {
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val cancellationToken = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken.token
        ).addOnCompleteListener {
            viewModel.makeRequest("${it.result.latitude},${it.result.longitude}")
        }
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

    private fun isLocationEnabled(): Boolean {
        val locationServices =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationServices.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkLocation() {
        if (isLocationEnabled()) getLocation()
        else {
            showLocationDialog {
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    private fun showTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
        }.attach()
    }

    private fun checkResponseForError() {
        viewModel.toastCode.observe(viewLifecycleOwner) { code ->
            when (code) {
                AppCompatActivity.RESULT_OK -> showToast(getString(R.string.something_went_wrong))
                AppCompatActivity.RESULT_CANCELED -> showToast(getString(R.string.something_wrong_with_server))
            }
        }
    }

    private fun showCurrentForecast() = with(binding) {
        viewModel.forecastAtPresent.observe(viewLifecycleOwner) { forecast ->
            val currentTempValue = "${forecast.currentTemp}°C"
            val minTempValue = "${forecast.minTemp}°"
            val maxTempValue = "${forecast.maxTemp}°"
            val feelingTempValue = "${forecast.feelingTemp}°"

            cardMainDate.text = dateTimeConverter.convertDate(forecast.date, 1)
            cardMainCurrentTemperature.text = currentTempValue
            cardMainCity.text = forecast.city
            cardMainDescription.text = forecast.weatherDescription
            cardMainMinTemp.text = minTempValue
            cardMainMaxTemp.text = maxTempValue
            cardMainFeelingTempValue.text = feelingTempValue
            Picasso.get().load("https:" + forecast.imageURL).into(cardMainWeatherIcon)
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }
}