package com.example.weatherforecast.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHourlyForecastBinding
import com.example.weatherforecast.model.forecastCreators.HourlyForecastCreator
import com.example.weatherforecast.model.models.WeatherData
import com.example.weatherforecast.utils.adapters.WeatherAdapter

class HourlyForecastFragment : Fragment(R.layout.fragment_hourly_forecast) {

    private lateinit var binding: FragmentHourlyForecastBinding
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: WeatherAdapter
    private val hourlyForecastCreator = HourlyForecastCreator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHourlyForecastBinding.bind(view)

        initRecView()
        initHourlyForecastObserver()
    }

    private fun initHourlyForecastObserver() {
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { weatherDataList ->
            val forecastForToday: WeatherData = weatherDataList[0]
            val forecastForTomorrow: WeatherData = weatherDataList[1]

            val hourlyForecast: List<WeatherData> =
                hourlyForecastCreator.getHourlyForecast(
                    forecastForToday,
                    forecastForTomorrow
                )
            adapter.submitList(hourlyForecast)
        }
    }

    private fun initRecView() = with(binding) {
        recView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherAdapter()
        recView.adapter = adapter
    }

}