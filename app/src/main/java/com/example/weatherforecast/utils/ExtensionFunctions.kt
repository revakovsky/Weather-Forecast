package com.example.weatherforecast.utils

import android.content.pm.PackageManager
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherforecast.R

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

fun Fragment.showLocationDialog(
    onClickPositive: () -> Unit
) {
    AlertDialog.Builder(requireContext())
        .setIcon(R.drawable.question_icon)
        .setTitle(getString(R.string.enable_location))
        .setMessage(getString(R.string.enable_location_description))
        .setPositiveButton(getString(R.string.yes)) { _, _ ->
            onClickPositive()
        }
        .setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        .create()
        .show()
}