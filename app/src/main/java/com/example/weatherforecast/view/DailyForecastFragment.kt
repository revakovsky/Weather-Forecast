package com.example.weatherforecast.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentDailyForecastBinding

class DailyForecastFragment : Fragment(R.layout.fragment_daily_forecast) {

    private lateinit var binding: FragmentDailyForecastBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDailyForecastBinding.bind(view)

    }

}