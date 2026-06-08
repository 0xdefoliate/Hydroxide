package se.axelkarlsson.hydroxide.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import se.axelkarlsson.hydroxide.app.App

@Composable
fun AppItem(app: App, onClick: () -> Unit = {}, onLongClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Image(
            bitmap = app.metadata.icon.rounded?.asImageBitmap()
                ?: app.metadata.icon.drawable.toBitmap(
                    app.metadata.icon.drawable.intrinsicWidth,
                    app.metadata.icon.drawable.intrinsicHeight
                ).asImageBitmap(),
            contentDescription = null
        )
    }
}