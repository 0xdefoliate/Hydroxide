package se.axelkarlsson.hydroxide.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun StatusBarShadow() {
    Box(
        modifier = Modifier
            .offset(y = (-32).dp)
            .fillMaxWidth()
            .height(
                WindowInsets.statusBars.getTop(
                    LocalDensity.current
                ).dp
            )
            .shadow(24.dp, shape = CircleShape)
    )
}