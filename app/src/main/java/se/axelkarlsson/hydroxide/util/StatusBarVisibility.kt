package se.axelkarlsson.hydroxide.util

import android.view.Window
import androidx.core.view.WindowInsetsCompat

object StatusBarVisibility: SystemBarVisibility() {
    override fun show(window: Window) {
        val insetsController = configure(window)
        insetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    override fun hide(window: Window) {
        val insetsController = configure(window)
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
    }
}