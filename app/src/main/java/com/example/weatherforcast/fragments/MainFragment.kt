package com.example.weatherforcast.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.weatherforcast.adapters.MyViewPagerAdapter
import com.example.weatherforcast.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val listOfFragments = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())
    private val listOfTabsNames = listOf("hours", "days")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initTabs()
    }

    fun initTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) {
            tab, position -> tab.text = listOfTabsNames[position].toString()
        }.attach()
    }

    private fun permissionListener() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}