package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.widget.RemoteViews
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.*
import com.alexandreladriere.f1companion.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException


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
        try {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.calendar_medium_widget)
            updateWidgetMediumCalendarUI(context, views, nextRace)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

fun updateWidgetMediumCalendarUI(context: Context, widgetView: RemoteViews, race: Race?) {
    // update title:
    widgetView.setTextViewText(R.id.textview_race_num, "R" + race?.round ?: "null")
    widgetView.setTextViewText(R.id.textview_race_locality, race?.circuit?.location?.locality ?: "null")
    // update we date
    val firstPracticeDate = utcToCurrentTimeZone(FORMAT_DATE, race?.firstPractice?.date.toString() + "T" +  race?.firstPractice?.time.toString())
    // FP2 should always exist
    val secondPracticeDate = utcToCurrentTimeZone(FORMAT_DATE, race?.secondPractice?.date.toString() + "T" +  race?.secondPractice?.time.toString())
    val raceDate = utcToCurrentTimeZone(FORMAT_DATE, race?.date.toString() + "T" +  race?.time.toString())
    val weStartingDay = getDayFromDate(firstPracticeDate)
    val weEndingDay = getDayFromDate(raceDate)
    val weMonth = MAP_MONTH[getMonthFromDate(raceDate)]
    widgetView.setTextViewText(R.id.textview_weekend_date, "$weStartingDay-$weEndingDay" ?: "null")
    widgetView.setTextViewText(R.id.textview_race_month, weMonth ?: "null")
    // update country flag
    val resources: Resources = context.resources
    val flagResourceId = resources.getIdentifier(race?.circuit?.location?.country.toString().replace(" ", "_").lowercase() + "", "drawable",
        context.packageName
    )
    widgetView.setImageViewResource(R.id.imageview_flag, flagResourceId)
    // update circuit layout
    val circuitLayoutResourceId = resources.getIdentifier(race?.circuit?.circuitId.toString().replace(" ", "_").lowercase() + "_layout", "drawable",
        context.packageName
    )
    widgetView.setImageViewResource(R.id.circuit_layout, circuitLayoutResourceId)
    // Update Session
    // FP1 should always be the firstSession, and Race the last one
    widgetView.setTextViewText(R.id.textview_race_first_session_hour, getHourMinuteFromDate(firstPracticeDate) ?: "null")
    widgetView.setTextViewText(R.id.textview_race_session_hour, getHourMinuteFromDate(raceDate) ?: "null")
    val hasSprint = race?.sprint != null
    if(hasSprint) {
        val sprintDate = utcToCurrentTimeZone(FORMAT_DATE, race?.sprint?.date.toString() + "T" +  race?.sprint?.time.toString())
        val qualifyingDate = utcToCurrentTimeZone(FORMAT_DATE, race?.qualifying?.date.toString() + "T" +  race?.qualifying?.time.toString())
        widgetView.setTextViewText(R.id.textview_second_session, "Q1" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_second_session_hour, getHourMinuteFromDate(qualifyingDate) ?: "null")

        widgetView.setTextViewText(R.id.textview_third_session, "FP2" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_third_session_hour, getHourMinuteFromDate(secondPracticeDate) ?: "null")

        widgetView.setTextViewText(R.id.textview_fourth_session, "Sprint" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_fourth_session_hour, getHourMinuteFromDate(sprintDate) ?: "null")
    } else {
        val thirdPracticeDate = utcToCurrentTimeZone(FORMAT_DATE, race?.thirdPractice?.date.toString() + "T" +  race?.thirdPractice?.time.toString())
        val qualifyingDate = utcToCurrentTimeZone(FORMAT_DATE, race?.qualifying?.date.toString() + "T" +  race?.qualifying?.time.toString())
        widgetView.setTextViewText(R.id.textview_second_session, "FP2" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_second_session_hour, getHourMinuteFromDate(secondPracticeDate) ?: "null")

        widgetView.setTextViewText(R.id.textview_third_session, "FP3" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_third_session_hour, getHourMinuteFromDate(thirdPracticeDate) ?: "null")

        widgetView.setTextViewText(R.id.textview_fourth_session, "Q1" ?: "null")
        widgetView.setTextViewText(R.id.textview_race_fourth_session_hour, getHourMinuteFromDate(qualifyingDate) ?: "null")
    }
}