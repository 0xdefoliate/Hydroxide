package se.axelkarlsson.hydroxide.app

import android.content.Context

sealed class AppLaunchException(message: String) : RuntimeException(message) {
    class InvalidLaunchIntent(packageName: String) :
        AppLaunchException("Failed to get a launch intent for $packageName!")
}

class App(val metadata: AppMetadata) {
    fun start(context: Context) {
        val pm = context.packageManager

        try {
            val intent = pm.getLaunchIntentForPackage(metadata.packageName)
                ?: throw AppLaunchException.InvalidLaunchIntent(metadata.packageName)

            context.startActivity(intent)
        } catch (exception: Exception) {
            TODO("Add exception handling!")
        }
    }
}