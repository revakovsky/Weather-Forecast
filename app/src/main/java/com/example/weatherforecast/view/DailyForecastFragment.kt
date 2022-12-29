package com.example.weatherforecast.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentDailyForecastBinding
import com.example.weatherforecast.utils.adapters.WeatherAdapter

class DailyForecastFragment : Fragment(R.layout.fragment_daily_forecast) {

    private lateinit var binding: FragmentDailyForecastBinding
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: WeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDailyForecastBinding.bind(view)

        initRecView()
        initDailyForecastObserver()
    }

    private fun initDailyForecastObserver() {
        viewModel.dailyForecast.observe(viewLifecycleOwner) { weatherDataList ->
            adapter.submitList(weatherDataList)
        }
    }

    private fun initRecView() = with(binding) {
        recView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherAdapter()
        recView.adapter = adapter
    }

}