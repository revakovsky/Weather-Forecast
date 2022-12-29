package com.example.weatherforecast.utils

import android.content.pm.PackageManager
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionProhibited(permission: String): Boolean {
    return ContextCompat
        .checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED
}

fun Fragment.showToast(text: String) {
    var toast = Toast(requireContext())
    toast.cancel()
    toast = makeText(requireContext(), text, Toast.LENGTH_SHORT)
    toast.show()
}