package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.RemoteViews
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.Race
import com.alexandreladriere.f1companion.datamodel.SeasonRacesResponse
import com.alexandreladriere.f1companion.datamodel.monthMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import java.text.SimpleDateFormat
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
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.calendar_medium_widget)
            updateWidgetCalendarUI(context, views, nextRace)
            //views.setTextViewText(R.id.race_num, nextRace?.raceName ?: "null")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
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

fun updateWidgetCalendarUI(context: Context, widgetView: RemoteViews, race: Race?) {
    // update title:
    widgetView.setTextViewText(R.id.textview_race_num, race?.round ?: "null")
    widgetView.setTextViewText(R.id.textview_race_locality, race?.circuit?.location?.locality ?: "null")
    // update we date
    val weDays = race?.firstPractice?.date.toString().takeLast(2) + "-" + race?.date.toString().takeLast(2)
    val weMonth = monthMap[race?.date.toString().substringAfter("-", "").substringBefore("-", "")]
    widgetView.setTextViewText(R.id.textview_weekend_date, weDays ?: "null")
    widgetView.setTextViewText(R.id.textview_race_month, weMonth ?: "null")
    // update country flag
    val resources: Resources = context.resources
    val flagResourceId = resources.getIdentifier(race?.circuit?.location?.country.toString().lowercase() + "", "drawable",
        context.packageName
    )
    widgetView.setImageViewResource(R.id.imageview_flag, flagResourceId)
    // update circuit layout
    val circuitLayoutResourceId = resources.getIdentifier(race?.circuit?.circuitId.toString().lowercase() + "_layout", "drawable",
        context.packageName
    )
    widgetView.setImageViewResource(R.id.circuit_layout, circuitLayoutResourceId)
    // Update Session
    // FP1 should always be the firstSession, and Race the last one
    widgetView.setTextViewText(R.id.textview_race_first_session_hour, race?.firstPractice?.time.toString().take(5) ?: "null")
    widgetView.setTextViewText(R.id.textview_race_session_hour, race?.time.toString().take(5) ?: "null")
    val hasSprint = race?.sprint != null
    if(hasSprint) {
        widgetView.setTextViewText(R.id.textview_second_session, "Q1" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_second_session_hour, race?.qualifying?.time.toString().take(5) ?: "null")

        widgetView.setTextViewText(R.id.textview_third_session, "FP2" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_third_session_hour, race?.secondPractice?.time.toString().take(5) ?: "null")

        widgetView.setTextViewText(R.id.textview_fourth_session, "Sprint" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_fourth_session_hour, race?.sprint?.time.toString().take(5) ?: "null")
    } else {
        widgetView.setTextViewText(R.id.textview_second_session, "FP2" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_second_session_hour, race?.secondPractice?.time.toString().take(5) ?: "null")

        widgetView.setTextViewText(R.id.textview_third_session, "FP3" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_third_session_hour, race?.thirdPractice?.time.toString().take(5) ?: "null")

        widgetView.setTextViewText(R.id.textview_fourth_session, "Q1" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_fourth_session_hour, race?.qualifying?.time.toString().take(5) ?: "null")
    }
}

