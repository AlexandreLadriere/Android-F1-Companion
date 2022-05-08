package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.season.standings.constructors.Standings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException


/**
 * Implementation of App Widget functionality.
 */
class ConstructorStandingsWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateConstructorStandingsWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateConstructorStandingsWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ergastApi = RetrofitHelper.getInstance().create(ErgastApi::class.java)
    // launching a new coroutine
    GlobalScope.launch {
        val result = ergastApi.getSeasonConstructorStandings()
        val constructorStandings = result.body()?.data?.standingsTable?.standingsList?.get(0)?.constructorStandings
        try {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.constructor_standings_widget)
            updateWidgetConstructorStandingsUI(context, views, constructorStandings)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

fun updateWidgetConstructorStandingsUI(context: Context, widgetView: RemoteViews, constructorStandingsList: List<Standings>?) {
    // RecyclerView not supported for widgets
    // 1
    if (constructorStandingsList != null) {
        for (i in constructorStandingsList.indices) {
            updateConstructorStanding(context, widgetView,
                constructorStandingsList[i], i+1)
        }
    }
}

fun updateConstructorStanding(context: Context, widgetView: RemoteViews, standing: Standings?, int: Int) {
    // Check Haas for text color
    val textColor = if (standing?.constructor?.constructorId == "haas") ResourcesCompat.getColor(context.resources, R.color.main_dark, null) else ResourcesCompat.getColor(context.resources, R.color.white, null)

    val idLayoutConstructorInfo: Int = context.resources.getIdentifier(
        "layout_constructor_" + int + "_info",
        "id",
        context.packageName
    )
    val idLayoutConstructorTeam: Int = context.resources.getIdentifier(
        "textview_constructor_" + int + "_team",
        "id",
        context.packageName
    )
    val idLayoutConstructorPoints: Int = context.resources.getIdentifier(
        "textview_constructor_" + int + "_points",
        "id",
        context.packageName
    )
    widgetView.setTextViewText(idLayoutConstructorTeam, standing?.constructor?.name ?: "null")
    widgetView.setTextColor(idLayoutConstructorTeam, textColor)
    widgetView.setTextViewText(idLayoutConstructorPoints, standing?.points ?: "null")
    widgetView.setTextColor(idLayoutConstructorPoints, textColor)
    val resources: Resources = context.resources
    val teamBackground = resources.getIdentifier("background_" + standing?.constructor?.constructorId.toString().replace(" ", "_").lowercase(), "drawable",
        context.packageName
    )
    widgetView.setInt(idLayoutConstructorInfo, "setBackgroundResource", teamBackground);
}