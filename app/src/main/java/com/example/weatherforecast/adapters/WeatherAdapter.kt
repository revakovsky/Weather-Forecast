package com.example.weatherforecast.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ListItemBinding

class WeatherAdapter : ListAdapter<WeatherData, WeatherAdapter.weatherViewHolder>(Comparator()) {

    class weatherViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)

        fun bind (item : WeatherData) = with(binding) {
            dateInfo.text = item.date
            weatherDescription.text = item.weatherDescription
            tempValue.text = item.currentTemp
            chanceOfRainValue.text = item.chanceOfRain
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): weatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return weatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: weatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class Comparator : DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }
    }
}