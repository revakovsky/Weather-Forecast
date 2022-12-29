package com.example.weatherforecast.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHourlyForecastBinding
import com.example.weatherforecast.utils.adapters.WeatherAdapter

class HourlyForecastFragment : Fragment(R.layout.fragment_hourly_forecast) {

    private lateinit var binding: FragmentHourlyForecastBinding
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: WeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHourlyForecastBinding.bind(view)

        initRecView()
        initHourlyForecastObserver()
    }

    private fun initHourlyForecastObserver() {
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { weatherDataList ->
            adapter.submitList(weatherDataList)
        }
    }

    private fun initRecView() = with(binding) {
        recView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherAdapter()
        recView.adapter = adapter
    }

}