package com.example.weatherforecast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.adapters.ForecastListCreator
import com.example.weatherforecast.MainViewModel
import com.example.weatherforecast.adapters.WeatherAdapter
import com.example.weatherforecast.adapters.WeatherData
import com.example.weatherforecast.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val forecastListCreator = ForecastListCreator()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()

        viewModel.hourlyForecastLiveData.observe(viewLifecycleOwner) {
            val hourlyForecastList: List<WeatherData> = forecastListCreator.getHourlyForecastList(it)
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
        fun newInstance() = HoursFragment()
    }
}