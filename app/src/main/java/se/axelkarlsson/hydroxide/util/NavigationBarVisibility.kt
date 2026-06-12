package se.axelkarlsson.hydroxide.util

import android.view.Window
import androidx.core.view.WindowInsetsCompat

object NavigationBarVisibility : SystemBarVisibility() {
    override fun show(window: Window) {
        val insetsController = configure(window)
        insetsController.show(WindowInsetsCompat.Type.navigationBars())
    }

    override fun hide(window: Window) {
        val insetsController = configure(window)
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}