package com.example.weatherforecast.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.view.currentforecast.CurrentForecastFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_activity_main, CurrentForecastFragment.newInstance()).commit()
    }

}