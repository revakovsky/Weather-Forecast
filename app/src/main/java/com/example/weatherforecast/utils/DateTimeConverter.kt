package com.example.weatherforecast.utils

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeConverter {

    fun convertDate(date: String): String {
        return parseDate(date)
    }

    @SuppressLint("NewApi")
    fun convertTime(time: String): String {
        return parseTime(time)
    }

    @Suppress("DEPRECATION")
    fun getCurrentTime(): Int {
        return Calendar.getInstance().time.hours
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SimpleDateFormat")
    private fun parseDate(dateTime: String): String {
        val format = "yyyy-MM-dd HH:mm"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern(format)
            val convertedDate = LocalDateTime.parse(dateTime, formatter)

            val month = changeDateSpelling(convertedDate.month.toString())
            val day = convertedDate.dayOfMonth
            val dayName = changeDateSpelling(convertedDate.dayOfWeek.toString())

            return "$dayName, $day of $month"

        } else {
            //todo
            val formatter = SimpleDateFormat(format)
            val date = formatter.parse(dateTime)

            val month = Calendar.getInstance().get(Calendar.MONTH)
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val dayName = changeDateSpelling(dayOfWeek.toString())

            return "$dayName, $day of $month"
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SimpleDateFormat")
    private fun parseTime(time: String): String {
        val format = "yyyy-MM-dd HH:mm"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern(format)
            val convertedTime = LocalDateTime.parse(time, formatter)

            val hour = convertedTime.hour
            val minute = convertedTime.minute

            return LocalTime.of(hour, minute).toString()

        } else {
            val formatter = SimpleDateFormat(format)
            val date = formatter.parse(time)

            val hour = if (date?.hours!! < 10) {
                "0${date.hours}"
            } else date.hours

            val minute = date.minutes

            return "$hour:${minute}0"
        }
    }

    private fun changeDateSpelling(dateItem: String): String {
        return dateItem.lowercase().replaceFirstChar { it.uppercase() }
    }
}