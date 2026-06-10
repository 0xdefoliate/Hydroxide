package se.axelkarlsson.hydroxide.app

import android.content.ComponentName

data class AppMetadata(
    val packageName: String,
    val label: String,
    val icon: AppIcon,
    val componentName: ComponentName
)
