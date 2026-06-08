package se.axelkarlsson.hydroxide.ui.route.homescreen

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.axelkarlsson.hydroxide.ui.route.drawer.DrawerScreen
import se.axelkarlsson.hydroxide.util.getScreenHeight
import kotlin.math.roundToInt

enum class HomeScreenDrawerAnchor {
    COLLAPSED,
    EXPANDED
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val collapsedHeight = getScreenHeight(LocalContext.current).toFloat()
    val expandedHeight = WindowInsets.statusBars.getBottom(LocalDensity.current).toFloat()

    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = HomeScreenDrawerAnchor.COLLAPSED,
            anchors = DraggableAnchors {
                HomeScreenDrawerAnchor.EXPANDED at expandedHeight
                HomeScreenDrawerAnchor.COLLAPSED at collapsedHeight
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .anchoredDraggable(
                anchoredDraggableState,
                orientation = Orientation.Vertical,
                enabled = true,
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(
                        x = 0,
                        y = anchoredDraggableState.requireOffset().roundToInt()
                    )
                }
        ) {
            DrawerScreen()
        }
    }
}