package se.axelkarlsson.hydroxide.ui.route.homescreen

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
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

    val scope = rememberCoroutineScope()

    // Manages the state of the App Drawer
    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = HomeScreenDrawerAnchor.COLLAPSED,
            anchors = DraggableAnchors {
                HomeScreenDrawerAnchor.EXPANDED at expandedHeight
                HomeScreenDrawerAnchor.COLLAPSED at collapsedHeight
            }
        )
    }

    // Resolves #7
    val nestedScrollConnection = remember(anchoredDraggableState) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y > 0) {
                    // This runs when the user swipes down
                    scope.launch {
                        anchoredDraggableState.animateTo(HomeScreenDrawerAnchor.COLLAPSED)
                    }

                    anchoredDraggableState.dispatchRawDelta(available.y)
                }

                return Offset.Zero
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val blur by remember {
        // cache the current scroll offset to increase performance
        derivedStateOf {
            // i.e. when the drawer is less than half the screen size, it will be blurred.
            if (anchoredDraggableState.requireOffset().roundToInt() > (collapsedHeight / 2).roundToInt()) {
                16.dp
            } else {
                0.dp
            }
        }
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
                .blur(blur)
                .nestedScroll(nestedScrollConnection)
        ) {
            DrawerScreen(anchoredDraggableState)
        }
    }
}