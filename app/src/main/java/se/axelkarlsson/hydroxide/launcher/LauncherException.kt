package se.axelkarlsson.hydroxide.launcher

sealed class LauncherException(message: String) : RuntimeException(message) {
    class FailedToGetLauncherAppsAPI() :
        LauncherException("Failed to retrieve an instance of LauncherApps!")
}