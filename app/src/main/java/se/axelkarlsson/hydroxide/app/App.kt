package se.axelkarlsson.hydroxide.app

import android.content.Context
import android.content.pm.LauncherApps
import android.graphics.RectF
import android.os.UserHandle
import androidx.core.content.getSystemService
import androidx.core.graphics.toRect
import se.axelkarlsson.hydroxide.PERMANENTLY_HIDDEN_APPS_BY_PACKAGE_NAMES
import se.axelkarlsson.hydroxide.launcher.LauncherException
import se.axelkarlsson.hydroxide.util.currentUserHandle

class App(val metadata: AppMetadata) {

    companion object {
        fun query(context: Context, userHandle: UserHandle = currentUserHandle()): List<App> {
            val launcherApps = context.getSystemService<LauncherApps>() ?: return emptyList()
            val density = context.resources.displayMetrics.densityDpi

            val apps = launcherApps.getActivityList(null, userHandle).mapNotNull {
                val drawable = it.getBadgedIcon(density)

                val metadata = AppMetadata(
                    packageName = it.activityInfo.packageName,
                    label = it.label.toString(),
                    icon = AppIcon(context, drawable),
                    componentName = it.componentName
                )

                if (metadata.packageName !in PERMANENTLY_HIDDEN_APPS_BY_PACKAGE_NAMES) {
                    App(metadata)
                } else {
                    null
                }
            }

            return apps
        }

        fun callback(context: Context, callback: LauncherApps.Callback) {
            val launcherApps = context.getSystemService<LauncherApps>()
                ?: throw LauncherException.FailedToGetLauncherAppsAPI()


            launcherApps.registerCallback(callback)
        }
    }

    fun start(
        context: Context, bounds: RectF?, userHandle: UserHandle = currentUserHandle()
    ) {
        val launcherApps = context.getSystemService<LauncherApps>()
            ?: throw LauncherException.FailedToGetLauncherAppsAPI()

        try {
            launcherApps.startMainActivity(
                metadata.componentName,
                userHandle,
                bounds?.toRect(),
                null
            )
        } catch (exception: Exception) {
            throw exception
        }
    }
}