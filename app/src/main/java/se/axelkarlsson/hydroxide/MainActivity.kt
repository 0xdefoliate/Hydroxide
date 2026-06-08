package se.axelkarlsson.hydroxide

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import se.axelkarlsson.hydroxide.ui.component.StatusBarShadow
import se.axelkarlsson.hydroxide.ui.route.homescreen.HomeScreen
import se.axelkarlsson.hydroxide.ui.theme.HydroxideTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                HomeScreen()
            }
        }
    }
}
