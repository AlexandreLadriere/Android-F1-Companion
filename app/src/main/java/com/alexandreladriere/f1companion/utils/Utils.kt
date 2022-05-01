package com.alexandreladriere.f1companion.utils

import java.text.SimpleDateFormat
import java.util.*


fun utcToCurrentTimeZone(dateFormat: String, dateStringToConvert: String): Date {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(dateStringToConvert) as Date
}

fun getHourMinuteFromDate(date: Date): String {
    val sdf = SimpleDateFormat(FORMAT_HOUR_MINUTE, Locale.getDefault())
    return sdf.format(date)
}

fun getDayFromDate(date: Date): String {
    val sdf = SimpleDateFormat(FORMAT_DAY, Locale.getDefault())
    return sdf.format(date)
}

fun getMonthFromDate(date: Date): String {
    val sdf = SimpleDateFormat(FORMAT_MONTH, Locale.getDefault())
    return sdf.format(date)
}