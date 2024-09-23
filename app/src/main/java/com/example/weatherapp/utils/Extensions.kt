package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Convert TimeStamp To DateTime String
 */
fun Int.getTimestampToDateTimeString(): String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

/**
 * Convert TimeStamp To Time String
 */
fun Int.getTimestampToTimeString(): String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}
