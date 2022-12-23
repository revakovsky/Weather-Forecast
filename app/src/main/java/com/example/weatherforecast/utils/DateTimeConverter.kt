package com.example.weatherforecast.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {

    fun convertDate(date: String): String {
        val convertedDate = parseDateTime(date)

        val month = changeDateOverview(convertedDate.month.toString())
        val day = convertedDate.dayOfMonth
        val dayName = changeDateOverview(convertedDate.dayOfWeek.toString())

        return "$dayName, $day of $month"
    }

    fun convertTime(time: String) : String {
        val convertedTime = parseDateTime(time)

        val hour = convertedTime.hour
        val minute = convertedTime.minute

        return LocalTime.of(hour, minute).toString()
    }

    fun getCurrentHour(): Int {
        return LocalTime.now().hour
    }

    private fun parseDateTime(dateTime : String) : LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(dateTime, formatter)
    }

    private fun changeDateOverview(dateItem: String) : String {
        return dateItem.lowercase().replaceFirstChar { it.uppercase() }
    }
}