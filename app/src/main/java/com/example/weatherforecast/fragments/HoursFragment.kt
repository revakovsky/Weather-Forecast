package com.example.weatherforecast.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.WeatherAdapter
import com.example.weatherforecast.adapters.WeatherData
import com.example.weatherforecast.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {
     private lateinit var binding: FragmentHoursBinding
     private lateinit var adapter: WeatherAdapter

     val list = listOf(
         WeatherData("", "12:00", "sunny", R.drawable.weather_icon.toString(), "25", "", "", "", "30"),
         WeatherData("", "09:00", "cloudy", R.drawable.weather_icon.toString(), "18", "", "", "", "50"),
         WeatherData("", "20:00", "rainy", R.drawable.weather_icon.toString(), "9", "", "", "", "80")
     )

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
    }

    private fun initRecView() = with(binding) {
        recViewHoursCards.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        recViewHoursCards.adapter = adapter
        adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}