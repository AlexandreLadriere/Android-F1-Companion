package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.SeasonRacesResponse
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
        try {
            val raceZero = result.body()?.data?.raceTable?.racesList?.get(0)
            Log.d("Retrofit: ", raceZero.toString())

            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.calendar_medium_widget)
            views.setTextViewText(R.id.appwidget_text, raceZero?.raceName ?: "null")
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

fun getNextRaceIndex(responseBody: SeasonRacesResponse) {

}

