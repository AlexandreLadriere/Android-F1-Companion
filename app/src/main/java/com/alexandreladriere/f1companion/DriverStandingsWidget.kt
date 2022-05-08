package com.alexandreladriere.f1companion

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Resources
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import com.alexandreladriere.f1companion.api.ErgastApi
import com.alexandreladriere.f1companion.api.RetrofitHelper
import com.alexandreladriere.f1companion.datamodel.season.standings.constructors.ConstructorStandings
import com.alexandreladriere.f1companion.datamodel.season.standings.drivers.DriverStandings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException

/**
 * Implementation of App Widget functionality.
 */
class DriverStandingsWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateDriverStandingsWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateDriverStandingsWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val ergastApi = RetrofitHelper.getInstance().create(ErgastApi::class.java)
    // launching a new coroutine
    GlobalScope.launch {
        val result = ergastApi.getSeasonDriverStandings()
        val driverStandings = result.body()?.data?.driverStandingsTable?.driverStandingsList?.get(0)?.driverStandings
        try {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.driver_standings_widget)
            updateWidgetDriverStandingsUI(context, views, driverStandings)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

fun updateWidgetDriverStandingsUI(context: Context, widgetView: RemoteViews, driverStandingsList: List<DriverStandings>?) {
    // RecyclerView not supported for widgets
    if (driverStandingsList != null) {
        for (i in driverStandingsList.indices) {
            updateDriverStanding(context, widgetView,
                driverStandingsList[i], i+1)
        }
    }
}

fun updateDriverStanding(context: Context, widgetView: RemoteViews, standing: DriverStandings?, int: Int) {
    // Check Haas for text color
    val textColor = if (standing?.constructor?.get(0)?.constructorId == "haas") ResourcesCompat.getColor(context.resources, R.color.main_dark, null) else ResourcesCompat.getColor(context.resources, R.color.white, null)

    val idLayoutDriverInfo: Int = context.resources.getIdentifier(
        "layout_driver_" + int + "_info",
        "id",
        context.packageName
    )
    val idLayoutDriverLastName: Int = context.resources.getIdentifier(
        "textview_driver_" + int + "_lastname",
        "id",
        context.packageName
    )
    val idLayoutDriverFirstName: Int = context.resources.getIdentifier(
        "textview_driver_" + int + "_firstname",
        "id",
        context.packageName
    )
    val idLayoutDriverPoints: Int = context.resources.getIdentifier(
        "textview_driver_" + int + "_points",
        "id",
        context.packageName
    )
    widgetView.setTextViewText(idLayoutDriverFirstName, standing?.driver?.givenName + " " ?: "null")
    widgetView.setTextColor(idLayoutDriverFirstName, textColor)
    widgetView.setTextViewText(idLayoutDriverLastName, standing?.driver?.familyName ?: "null")
    widgetView.setTextColor(idLayoutDriverLastName, textColor)
    widgetView.setTextViewText(idLayoutDriverPoints, standing?.points + " PTS" ?: "null")
    widgetView.setTextColor(idLayoutDriverPoints, textColor)
    val resources: Resources = context.resources
    val teamBackground = resources.getIdentifier("background_" + standing?.constructor?.get(0)?.constructorId.toString().replace(" ", "_").lowercase(), "drawable",
        context.packageName
    )
    widgetView.setInt(idLayoutDriverInfo, "setBackgroundResource", teamBackground);
}