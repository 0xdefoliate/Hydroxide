package se.axelkarlsson.hydroxide.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import se.axelkarlsson.hydroxide.app.App
import se.axelkarlsson.hydroxide.app.AppIcon
import se.axelkarlsson.hydroxide.app.AppMetadata

fun queryApps(context: Context): List<App> {
    val pm = context.packageManager

    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val apps = pm.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES)
        .map {
            val metadata = AppMetadata(
                packageName = it.activityInfo.packageName,
                label = it.loadLabel(pm).toString(),
                icon = AppIcon(context, it.loadIcon(pm))
            )

            App(metadata)
        }

    return apps
}