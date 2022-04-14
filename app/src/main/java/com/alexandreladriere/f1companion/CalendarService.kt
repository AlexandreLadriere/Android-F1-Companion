package com.alexandreladriere.f1companion


import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.Nullable


class CalendarService : Service() {
        @Nullable
        override fun onBind(intent: Intent?): IBinder? {
            return null
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            // get API info
            // Update widget View (https://medium.com/android-bits/android-widgets-ad3d166458d3)
            val view = RemoteViews(packageName, R.layout.calendar_medium_widget)
            view.setTextViewText(R.id.appwidget_text, "MAJ")
            val theWidget = ComponentName(this, CalendarMediumWidget::class.java)
            val manager = AppWidgetManager.getInstance(this)
            manager.updateAppWidget(theWidget, view)
            return super.onStartCommand(intent, flags, startId)
        }

}