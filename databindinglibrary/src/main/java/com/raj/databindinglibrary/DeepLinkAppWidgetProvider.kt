package com.raj.databindinglibrary

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder

class DeepLinkAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        val remoteViews =  RemoteViews(context?.packageName,R.layout.deep_link_appwidget)

        val args = Bundle()
        args.putString("myarg", "From Widget")
        // TODO STEP 10 - construct and set a PendingIntent using DeepLinkBuilder
        val pendingIntent = NavDeepLinkBuilder(context!!)
                .setGraph(R.navigation.mobile_navigation)
                .setDestination(R.id.thirdFragment)
                .setArguments(args)
                .createPendingIntent()

        remoteViews.setOnClickPendingIntent(R.id.deep_link_button, pendingIntent)
        // TODO END STEP 10
        appWidgetManager?.updateAppWidget(appWidgetIds, remoteViews)

    }
}