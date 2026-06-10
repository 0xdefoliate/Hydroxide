package se.axelkarlsson.hydroxide.app

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.UserHandle
import androidx.core.content.getSystemService
import se.axelkarlsson.hydroxide.launcher.LauncherException
import se.axelkarlsson.hydroxide.util.currentUserHandle

class App(val metadata: AppMetadata) {

    companion object {
        fun query(context: Context, userHandle: UserHandle = currentUserHandle()): List<App> {
            val launcherApps = context.getSystemService<LauncherApps>() ?: return emptyList()
            val density = context.resources.displayMetrics.densityDpi

            val apps = launcherApps.getActivityList(null, userHandle).map {
                val metadata = AppMetadata(
                    packageName = it.activityInfo.packageName,
                    label = it.label.toString(),
                    icon = AppIcon(context, it.getBadgedIcon(density)),
                    componentName = it.componentName
                )

                App(metadata)
            }

            return apps
        }

        fun callback(context: Context, callback: LauncherApps.Callback) {
            val launcherApps = context.getSystemService<LauncherApps>()
                ?: throw LauncherException.FailedToGetLauncherAppsAPI()


            launcherApps.registerCallback(callback)
        }
    }

    fun start(context: Context, userHandle: UserHandle = currentUserHandle()) {
        val launcherApps = context.getSystemService<LauncherApps>()
            ?: throw LauncherException.FailedToGetLauncherAppsAPI()

        try {
            launcherApps.startMainActivity(metadata.componentName, userHandle, null, Bundle())
        } catch (exception: Exception) {
            throw exception
        }
    }
}