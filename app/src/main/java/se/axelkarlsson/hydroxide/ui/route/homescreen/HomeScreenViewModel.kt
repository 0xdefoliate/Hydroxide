package se.axelkarlsson.hydroxide.ui.route.homescreen

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import se.axelkarlsson.hydroxide.app.App
import se.axelkarlsson.hydroxide.util.queryApps
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val apps = MutableStateFlow<List<App>>(emptyList())

    fun load() {
        apps.value = queryApps(context)
    }
}