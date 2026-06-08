package se.axelkarlsson.hydroxide.util

import android.content.Context

fun getScreenHeight(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
}