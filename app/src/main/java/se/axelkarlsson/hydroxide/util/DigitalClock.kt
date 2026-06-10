package se.axelkarlsson.hydroxide.util

import android.content.Context
import android.text.format.DateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private object TimePattern {
    fun current(context: Context): String {
        return if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh:mm"
        }
    }
}

fun digitalClock(context: Context): String =
    LocalTime.now().format(DateTimeFormatter.ofPattern(TimePattern.current(context)))