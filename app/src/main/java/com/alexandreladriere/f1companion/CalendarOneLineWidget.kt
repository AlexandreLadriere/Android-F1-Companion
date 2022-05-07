package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.widget.RemoteViews
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.Race
import com.alexandreladriere.f1companion.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException

/**
 * Implementation of App Widget functionality.
 */
class CalendarOneLineWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateOneLineCalendarWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateOneLineCalendarWidget(
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
            val views = RemoteViews(context.packageName, R.layout.calendar_one_line_widget)
            updateWidgetOneLineCalendarUI(context, views, nextRace)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

fun updateWidgetOneLineCalendarUI(context: Context, widgetView: RemoteViews, race: Race?) {
    widgetView.setTextViewText(R.id.textview_one_line_calendar_race_num, "R" + race?.round ?: "null")
    widgetView.setTextViewText(R.id.textview_one_line_calendar_race_locality, race?.circuit?.location?.locality ?: "null")
    // update we date
    widgetView.setTextViewText(R.id.textview_one_line_calendar_race_date, race?.date ?: "null")
    val raceDate = utcToCurrentTimeZone(FORMAT_DATE, race?.date.toString() + "T" +  race?.time.toString())
    widgetView.setTextViewText(R.id.textview_one_line_calendar_race_hour, getHourMinuteFromDate(raceDate) ?: "null")
    // update circuit layout
    val resources: Resources = context.resources
    val circuitLayoutResourceId = resources.getIdentifier(race?.circuit?.circuitId.toString().replace(" ", "_").lowercase() + "_layout", "drawable",
        context.packageName
    )
    widgetView.setImageViewResource(R.id.imageview_one_line_calendar_flag, circuitLayoutResourceId)

}