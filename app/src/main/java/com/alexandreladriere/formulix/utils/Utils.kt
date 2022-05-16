package com.alexandreladriere.formulix.utils

import com.alexandreladriere.formulix.datamodel.season.races.SeasonRacesResponse
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

fun getNextRaceIndex(responseBody: SeasonRacesResponse?): Int {
    // not using Instant instant = Instant.now(); because it requires API26 at least
    val currentDate: Date = getCurrentDate()
    var raceDatePrevious: Date
    var raceDateNext: Date
    if (responseBody != null) {
        for (i in 0 until (responseBody.data?.raceTable?.racesList?.size?.minus(1) ?: 0)) {
            val currentRaceListObject = responseBody.data?.raceTable?.racesList?.get(i)
            val nextRaceListObject = responseBody.data?.raceTable?.racesList?.get(i + 1)
            raceDatePrevious = utcToCurrentTimeZone(FORMAT_DATE, currentRaceListObject?.date.toString() + "T" +  currentRaceListObject?.time.toString())
            raceDateNext = utcToCurrentTimeZone(FORMAT_DATE, nextRaceListObject?.date.toString() + "T" +  nextRaceListObject?.time.toString())
            val cmpPrevious = currentDate.compareTo(raceDatePrevious)
            val cmpNext = currentDate.compareTo(raceDateNext)
            if(cmpPrevious > 0 && cmpNext < 0) {
                return i + 1
            }
        }
    }
    return -1
}

fun getCurrentDate(): Date {
    val sdf = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
    val currentDateStr = sdf.format(Date())
    return sdf.parse(currentDateStr) as Date
}