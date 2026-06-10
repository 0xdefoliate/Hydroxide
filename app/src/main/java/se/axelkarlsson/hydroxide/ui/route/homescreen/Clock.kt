package se.axelkarlsson.hydroxide.ui.route.homescreen

import android.app.WallpaperColors
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toColorLong

@Composable
fun Clock(time: String, palette: WallpaperColors? = null) {
    val colour = if (palette == null) {
        Color.White
    } else if (palette.colorHints and WallpaperColors.HINT_SUPPORTS_DARK_TEXT != 0) {
        Color(
            ColorUtils.blendARGB(
                palette.primaryColor.toArgb(),
                android.graphics.Color.BLACK.toColorLong().toColorInt(),
                0.7f
            )
        )
    } else {
        Color(
            ColorUtils.blendARGB(
                palette.primaryColor.toArgb(),
                android.graphics.Color.WHITE.toColorLong().toColorInt(),
                0.7f
            )
        )
    }

    Text(
        text = time,
        modifier = Modifier.padding(24.dp),
        fontSize = 64.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        color = colour
    )
}