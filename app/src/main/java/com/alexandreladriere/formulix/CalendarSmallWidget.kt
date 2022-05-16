package com.alexandreladriere.formulix

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.alexandreladriere.formulix.R
import com.alexandreladriere.formulix.api.ErgastApi
import com.alexandreladriere.formulix.api.RetrofitHelper
import com.alexandreladriere.formulix.datamodel.season.races.Race
import com.alexandreladriere.formulix.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException

/**
 * Implementation of App Widget functionality.
 */
class CalendarSmallWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateSmallCalendarWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateSmallCalendarWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ergastApi = RetrofitHelper.getInstance().create(ErgastApi::class.java)
    // launching a new coroutine
    GlobalScope.launch {
        val result = ergastApi.getSeasonRaces()
        val nextRace = result.body()?.data?.raceTable?.racesList?.get(getNextRaceIndex(result.body()))
        try {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.calendar_small_widget)
            updateWidgetSmallCalendarUI(context, views, nextRace)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

fun updateWidgetSmallCalendarUI(context: Context, widgetView: RemoteViews, race: Race?) {
    // Update title
    widgetView.setTextViewText(R.id.textview_small_calendar_race_locality, race?.circuit?.location?.locality ?: "null")
    // Update Date
    val firstPracticeDate = utcToCurrentTimeZone(FORMAT_DATE, race?.firstPractice?.date.toString() + "T" +  race?.firstPractice?.time.toString())
    val raceDate = utcToCurrentTimeZone(FORMAT_DATE, race?.date.toString() + "T" +  race?.time.toString())
    val weStartingDay = getDayFromDate(firstPracticeDate)
    val weEndingDay = getDayFromDate(raceDate)
    val weMonth = MAP_MONTH[getMonthFromDate(raceDate)]
    widgetView.setTextViewText(R.id.textview_small_calendar_weekend_date, "$weStartingDay-$weEndingDay" ?: "null")
    widgetView.setTextViewText(R.id.textview_small_calendar_race_month, " $weMonth" ?: "null")
    // Update remaining days and hours
    val diff: Long = raceDate.time - getCurrentDate().time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val remainingHours = hours % 24
    val days = hours / 24
    // for hour(s) abbreviation, see: https://english.stackexchange.com/questions/380296/hour-abbreviation-h-or-h
    val hourAbr = if (remainingHours >= 2) " hrs" else " hr"
    val dayAbr = if (days >= 2) " days" else " day"

    widgetView.setTextViewText(R.id.textview_small_days_remaining, "$days $dayAbr, $remainingHours$hourAbr"
        ?: "null")
    // Update place
    widgetView.setTextViewText(R.id.textview_small_gp, race?.circuit?.location?.country ?: "null")
    widgetView.setTextViewText(R.id.textview_small_circuit_name, race?.circuit?.circuitName ?: "null")
}
