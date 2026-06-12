package se.axelkarlsson.hydroxide.ui.route.drawer

import android.app.WallpaperColors
import android.graphics.Color
import android.graphics.RectF
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toColorLong
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.axelkarlsson.hydroxide.extension.shouldUseDarkTheme
import se.axelkarlsson.hydroxide.launcher.AppItemPositionTracker
import se.axelkarlsson.hydroxide.ui.component.AppItem

@Composable
fun DrawerScreen(
    appItemPositionTracker: AppItemPositionTracker,
    palette: WallpaperColors?,
    viewModel: DrawerViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .clip(shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
            .background(
                color = when {
                    palette == null -> MaterialTheme.colorScheme.inverseSurface
                    palette.shouldUseDarkTheme -> androidx.compose.ui.graphics.Color(
                        ColorUtils.blendARGB(
                            palette.primaryColor.toArgb(),
                            Color.WHITE.toColorLong().toColorInt(),
                            0.55f
                        )
                    )

                    else -> androidx.compose.ui.graphics.Color(
                        ColorUtils.blendARGB(
                            palette.primaryColor.toArgb(),
                            Color.BLACK.toColorLong().toColorInt(),
                            0.55f
                        )
                    )
                }
            )
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(
                    WindowInsets.statusBars.asPaddingValues(
                        LocalDensity.current
                    )
                )
                .padding(horizontal = 12.dp),
            columns = GridCells.Adaptive(minSize = 80.dp)
        ) {
            items(apps) {
                val bounds = remember { mutableStateOf<RectF?>(null) }

                AppItem(it, onClick = {

                    // NOTE: This is temporarily disabled since it actually
                    // decreases UX now that we've implemented the GestureNavContract.
                    //
                    // Resolves #6
                    /*scope.launch {
                        anchoredDraggableState.animateTo(HomeScreenDrawerAnchor.COLLAPSED)
                    }*/

                    viewModel.onAppItemTap(it, appItemPositionTracker)
                }, onLongClick = {
                    viewModel.onAppItemLongTap(it)
                }, bounds = bounds)

                LaunchedEffect(bounds) {
                    val rect = bounds.value

                    if (rect != null) {
                        appItemPositionTracker.update(it, rect)
                    } else {
                        appItemPositionTracker.remove(it)
                    }
                }
            }
        }
    }
}