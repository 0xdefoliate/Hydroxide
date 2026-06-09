package se.axelkarlsson.hydroxide.ui.route.homescreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toColorLong

@Composable
fun Clock(time: String, colour: android.graphics.Color? = null) {
    Text(
        text = time,
        modifier = Modifier.padding(24.dp),
        fontSize = 64.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        color = Color(
            ColorUtils.blendARGB(
                MaterialTheme.colorScheme.primary.toArgb(),
                ColorUtils.blendARGB(
                    colour?.toArgb() ?: android.graphics.Color.BLACK.toColorLong().toColorInt(),
                    android.graphics.Color.WHITE.toColorLong().toColorInt(),
                    0.65f
                ),
                0.9f
            )
        )
    )
}