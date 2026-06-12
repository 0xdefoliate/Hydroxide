package se.axelkarlsson.hydroxide.launcher

import android.content.ComponentName
import android.graphics.RectF
import se.axelkarlsson.hydroxide.app.App
import javax.inject.Inject

class AppItemPositionTracker @Inject constructor() {
    private val tracker = mutableMapOf<String, RectF>()

    fun update(app: App, bounds: RectF) {
        tracker[app.metadata.componentName.packageName] = bounds
    }

    fun remove(app: App) {
        tracker.remove(app.metadata.componentName.packageName)
    }

    fun get(componentName: ComponentName): RectF? = tracker[componentName.packageName]
}