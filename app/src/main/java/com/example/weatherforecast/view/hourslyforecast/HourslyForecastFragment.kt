package com.example.weatherforecast.view.hourslyforecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.adapters.WeatherAdapter
import com.example.weatherforecast.adapters.WeatherData
import com.example.weatherforecast.databinding.FragmentHourslyForecastBinding
import com.example.weatherforecast.forecastCreators.HourlyForecastListCreator

class HourslyForecastFragment : Fragment() {
    private lateinit var binding: FragmentHourslyForecastBinding
    private lateinit var adapter: WeatherAdapter
    private val hourlyForecastListCreator = HourlyForecastListCreator()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHourslyForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()

        viewModel.hourlyForecastLiveData.observe(viewLifecycleOwner) {
            val forecastDataAtPresent = it[0]
            val tomorrowDayForecast = it[1]

            val hourlyForecastList: List<WeatherData> =
                hourlyForecastListCreator.getHourlyForecastList(forecastDataAtPresent, tomorrowDayForecast)

            adapter.submitList(hourlyForecastList)
        }
    }

    private fun initRecView() = with(binding) {
        recViewHoursCards.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        recViewHoursCards.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HourslyForecastFragment()
    }
}