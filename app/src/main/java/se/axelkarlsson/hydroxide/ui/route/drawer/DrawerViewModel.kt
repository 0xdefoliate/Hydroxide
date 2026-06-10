package se.axelkarlsson.hydroxide.ui.route.drawer

import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.axelkarlsson.hydroxide.app.App
import javax.inject.Inject

private class DrawerLauncherCallback(
    private val context: Context,
    private val apps: MutableStateFlow<List<App>>
) : LauncherApps.Callback() {
    override fun onPackageAdded(p0: String?, p1: UserHandle?) {
        apps.value = App.query(context)
    }

    override fun onPackageChanged(p0: String?, p1: UserHandle?) {
        apps.value = App.query(context)
    }

    override fun onPackageRemoved(p0: String?, p1: UserHandle?) {
        apps.value = App.query(context)
    }

    override fun onPackagesAvailable(
        p0: Array<out String?>?,
        p1: UserHandle?,
        p2: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onPackagesUnavailable(
        p0: Array<out String?>?,
        p1: UserHandle?,
        p2: Boolean
    ) {
        TODO("Not yet implemented")
    }

}

@HiltViewModel
class DrawerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _apps = MutableStateFlow<List<App>>(emptyList())
    val apps: StateFlow<List<App>> = _apps

    init {
        _apps.value = App.query(context)

        App.callback(
            context, DrawerLauncherCallback(
                context,
                _apps
            )
        )
    }

    fun onAppItemTap(app: App) {
        app.start(context)
    }

    fun onAppItemLongTap(app: App) {

    }
}