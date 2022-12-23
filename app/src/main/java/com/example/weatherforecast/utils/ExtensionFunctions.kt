package com.example.weatherforecast.utils

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionGranted(permission: String) : Boolean {
    return ContextCompat
        .checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
}