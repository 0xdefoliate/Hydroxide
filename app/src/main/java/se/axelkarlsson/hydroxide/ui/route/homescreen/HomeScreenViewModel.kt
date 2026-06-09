package se.axelkarlsson.hydroxide.ui.route.homescreen

import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
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
import se.axelkarlsson.hydroxide.util.queryApps
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val wallpaperManager = WallpaperManager.getInstance(context)

    private val _apps = MutableStateFlow<List<App>>(emptyList())
    private val _time =
        MutableStateFlow<String>(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))

    val apps: StateFlow<List<App>> = _apps
    val time: StateFlow<String> = _time

    val palette = MutableStateFlow<WallpaperColors?>(null)

    fun load() {
        _apps.value = queryApps(context)

        viewModelScope.launch {
            val colours = wallpaperManager.getWallpaperColors(WallpaperManager.FLAG_SYSTEM)

            if (colours != null) {
                palette.value = colours
            }
        }

        viewModelScope.launch {
            while (isActive) {
                _time.value = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                delay(300.toDuration(DurationUnit.MILLISECONDS))
            }
        }
    }
}