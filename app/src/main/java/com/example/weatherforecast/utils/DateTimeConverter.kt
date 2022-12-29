package com.example.weatherforecast.utils

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeConverter {

    fun convertDate(date: String, index: Int): String {
        val format1 = "yyyy-MM-dd HH:mm"
        val format2 = "yyyy-MM-dd"

        return when(index) {
            1 -> parseDate(date, format1)
            else -> parseDate(date, format2)
        }
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
    private fun parseDate(dateTime: String, format: String): String {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (format == "yyyy-MM-dd HH:mm") {
                val formatter = DateTimeFormatter.ofPattern(format)
                val convertedDate = LocalDateTime.parse(dateTime, formatter)

                val month = changeDateSpelling(convertedDate.month.toString())
                val day = convertedDate.dayOfMonth
                val dayName = changeDateSpelling(convertedDate.dayOfWeek.toString())

                return "$dayName, $day of $month"
            } else {
                val formatter = DateTimeFormatter.ofPattern(format)
                val convertedDate = LocalDate.parse(dateTime, formatter)

                val month = changeDateSpellingShort(convertedDate.month.toString())
                val day = convertedDate.dayOfMonth

                return "$day $month"
            }

        } else {
            if (format == "yyyy-MM-dd HH:mm") {
                val simpleFormat = SimpleDateFormat(format)
                val date = simpleFormat.parse(dateTime)

                val month = DateFormat.format("MMMM", date) as String
                val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val dayName = changeDateSpelling(DateFormat.format("EEEE", date) as String)

                return "$dayName, $day of $month"
            } else {
                val simpleFormat = SimpleDateFormat(format)
                val date = simpleFormat.parse(dateTime)

                val month = DateFormat.format("MMM", date) as String
                val day = DateFormat.format("dd",   date) as String

                return "$day $month"
            }
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

    private fun changeDateSpellingShort(dateItem: String): String {
        return dateItem.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    }
}