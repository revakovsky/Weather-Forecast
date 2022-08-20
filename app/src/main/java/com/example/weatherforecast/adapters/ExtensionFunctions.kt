package com.example.weatherforecast.fragments

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionGranted(permission: String) : Boolean {
    return ContextCompat
        .checkSelfPermission(activity as AppCompatActivity, permission) == PackageManager.PERMISSION_GRANTED

    //PackageManager.PERMISSION_GRANTED = 0
    //PackageManager.PERMISSION_DENIED = -1
}