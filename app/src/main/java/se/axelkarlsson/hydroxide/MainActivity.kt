package se.axelkarlsson.hydroxide

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.launcher3.GestureNavContract
import dagger.hilt.android.AndroidEntryPoint
import se.axelkarlsson.hydroxide.launcher.AppItemPositionTracker
import se.axelkarlsson.hydroxide.ui.component.StatusBarShadow
import se.axelkarlsson.hydroxide.ui.route.homescreen.HomeScreen
import se.axelkarlsson.hydroxide.ui.theme.HydroxideTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appItemPositionTracker = AppItemPositionTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                scrim = Color.TRANSPARENT,
            ),

            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.TRANSPARENT,
            )
        )

        window.isNavigationBarContrastEnforced = false

        setContent {
            HydroxideTheme {
                StatusBarShadow()
                HomeScreen(appItemPositionTracker)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        if (intent.hasExtra(GestureNavContract.EXTRA_GESTURE_CONTRACT)) {
            onGestureHome(intent)
        }
    }

    private fun onGestureHome(intent: Intent) {
        val contract = GestureNavContract.fromIntent(intent) ?: return
        val position = appItemPositionTracker.get(contract.componentName)

        contract.sendEndPosition(position)
    }
}
