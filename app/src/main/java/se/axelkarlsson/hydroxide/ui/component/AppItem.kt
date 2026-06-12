package se.axelkarlsson.hydroxide.ui.component

import android.graphics.RectF
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import se.axelkarlsson.hydroxide.app.App

@Composable
fun AppItem(
    app: App,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    size: Dp = 72.dp,
    bounds: MutableState<RectF?>
) {
    var pressed by remember {
        mutableStateOf(false)
    }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 1.15f else 1f,
        animationSpec = tween(durationMillis = 170, easing = Ease)
    )

    Column(
        modifier = Modifier
            .padding(12.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() },
                    onPress = {
                        pressed = true

                        tryAwaitRelease()
                        pressed = false
                    })
            }
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .onGloballyPositioned { coordinates ->
                bounds.value = coordinates.boundsInParent().toAndroidRectF()
            }) {
        Image(
            bitmap = app.metadata.icon.rounded?.asImageBitmap()
                ?: app.metadata.icon.drawable.toBitmap(
                    app.metadata.icon.drawable.intrinsicWidth,
                    app.metadata.icon.drawable.intrinsicHeight
                ).asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}