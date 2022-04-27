package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.SeasonRacesResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class CalendarMediumWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
         for (appWidgetId in appWidgetIds) {
             updateAppWidget(context, appWidgetManager, appWidgetId)
         }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ergastApi = RetrofitHelper.getInstance().create(ErgastApi::class.java)
    // launching a new coroutine
    GlobalScope.launch {
        val result = ergastApi.getSeasonRaces()
        val nextRace = result.body()?.data?.raceTable?.racesList?.get(getNextRaceIndex(result.body()))
        Log.d("NextRace: ",
            nextRace.toString()
        )
        try {
            // Log.d("Retrofit: ", raceZero.toString())

            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.calendar_medium_widget)
            //views.setTextViewText(R.id.race_num, nextRace?.raceName ?: "null")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    // val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    // val views = RemoteViews(context.packageName, R.layout.calendar_medium_widget)
    // views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    // appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun getNextRaceIndex(responseBody: SeasonRacesResponse?): Int {
    // not using Instant instant = Instant.now(); because it requires API26 at least
    // TODO: getLocal in Zero UTC
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentDateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    val currentDate: Date = sdf.parse(currentDateStr) as Date
    var raceDatePrevious: Date
    var raceDateNext: Date
    if (responseBody != null) {
        for (i in 0 until (responseBody.data?.raceTable?.racesList?.size?.minus(1) ?: 0)) {
            val currentRaceListObject = responseBody.data?.raceTable?.racesList?.get(i)
            val nextRaceListObject = responseBody.data?.raceTable?.racesList?.get(i + 1)
            raceDatePrevious = sdf.parse(currentRaceListObject?.date.toString() + " " +  currentRaceListObject?.time.toString().dropLast(1)) as Date
            raceDateNext = sdf.parse(nextRaceListObject?.date.toString() + " " +  nextRaceListObject?.time.toString().dropLast(1)) as Date
            val cmpPrevious = currentDate.compareTo(raceDatePrevious)
            val cmpNext = currentDate.compareTo(raceDateNext)
            if(cmpPrevious > 0 && cmpNext < 0) {
                return i + 1
            }
        }
    }
    return -1
}

