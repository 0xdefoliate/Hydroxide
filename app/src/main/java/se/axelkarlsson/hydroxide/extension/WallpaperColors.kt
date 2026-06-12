package se.axelkarlsson.hydroxide.extension

import android.app.WallpaperColors

val WallpaperColors.shouldUseDarkTheme: Boolean
    get() = colorHints and WallpaperColors.HINT_SUPPORTS_DARK_THEME != 0

val WallpaperColors.shouldUseDarkText: Boolean
    get() = colorHints and WallpaperColors.HINT_SUPPORTS_DARK_TEXT != 0