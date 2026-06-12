package se.axelkarlsson.hydroxide.ui.route.homescreen

import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import se.axelkarlsson.hydroxide.app.App
import se.axelkarlsson.hydroxide.util.digitalClock
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private class OnWallpaperColoursChanged(
        private val palette: MutableStateFlow<WallpaperColors?>
    ) : WallpaperManager.OnColorsChangedListener {
        override fun onColorsChanged(p0: WallpaperColors?, p1: Int) {
            if (p1 == WallpaperManager.FLAG_SYSTEM) {
                palette.value = p0
            }
        }
    }

    private val wallpaperManager = WallpaperManager.getInstance(context)

    private val _apps = MutableStateFlow<List<App>>(emptyList())
    private val _time = MutableStateFlow(digitalClock(context))
    private val _palette = MutableStateFlow<WallpaperColors?>(null)

    val apps: StateFlow<List<App>> = _apps
    val time: StateFlow<String> = _time

    val palette: StateFlow<WallpaperColors?> = _palette

    init {
        _apps.value = App.query(context)

        wallpaperManager.addOnColorsChangedListener(
            OnWallpaperColoursChanged(_palette), Handler(
                Looper.getMainLooper()
            )
        )

        viewModelScope.launch {
            _palette.value = wallpaperManager.getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
        }

        viewModelScope.launch {
            while (isActive) {
                _time.value = digitalClock(context)
                delay(300.toDuration(DurationUnit.MILLISECONDS))
            }
        }
    }
}