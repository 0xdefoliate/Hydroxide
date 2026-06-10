package se.axelkarlsson.hydroxide.util

import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

object NavigationBarVisibility {

    private fun configure(window: Window): WindowInsetsControllerCompat {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        return insetsController
    }

    fun show(window: Window) {
        val insetsController = configure(window)
        insetsController.show(WindowInsetsCompat.Type.navigationBars())
    }

    fun hide(window: Window) {
        val insetsController = configure(window)
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}