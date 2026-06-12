package se.axelkarlsson.hydroxide.ui.route.homescreen

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import se.axelkarlsson.hydroxide.DRAWER_SWIPE_DOWN_THRESHOLD_PIXELS
import se.axelkarlsson.hydroxide.launcher.AppItemPositionTracker
import se.axelkarlsson.hydroxide.ui.route.drawer.DrawerScreen
import se.axelkarlsson.hydroxide.util.NavigationBarVisibility
import se.axelkarlsson.hydroxide.util.StatusBarVisibility
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

enum class HomeScreenDrawerAnchor {
    COLLAPSED, EXPANDED
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(
    appItemPositionTracker: AppItemPositionTracker, viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val window = LocalActivity.current?.window
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val collapsedHeight = density.run { windowInfo.containerSize.height.toDp().toPx() }
    val expandedHeight = 0f

    val scope = rememberCoroutineScope()

    var job by remember {
        mutableStateOf<Job?>(null)
    }

    val palette = viewModel.palette
    val time by viewModel.time.collectAsStateWithLifecycle()


    // Manages the state of the App Drawer
    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = HomeScreenDrawerAnchor.COLLAPSED,
            anchors = DraggableAnchors {
                HomeScreenDrawerAnchor.EXPANDED at expandedHeight
                HomeScreenDrawerAnchor.COLLAPSED at collapsedHeight
            },
        )
    }

    val drawerOffset by remember {
        derivedStateOf {
            anchoredDraggableState.requireOffset()
        }
    }

    val clockVisible by remember {
        derivedStateOf {
            anchoredDraggableState.currentValue == HomeScreenDrawerAnchor.EXPANDED
        }
    }

    LaunchedEffect(anchoredDraggableState.currentValue, drawerOffset) {
        if (window == null) {
            return@LaunchedEffect
        }

        if (anchoredDraggableState.currentValue == HomeScreenDrawerAnchor.EXPANDED) {
            NavigationBarVisibility.hide(window)
            StatusBarVisibility.show(window)
        } else {
            NavigationBarVisibility.show(window)
        }

        if (drawerOffset >= collapsedHeight) {
            StatusBarVisibility.hide(window)
        } else {
            StatusBarVisibility.show(window)
        }
    }

    // Resolves #7
    val nestedScrollConnection = remember(anchoredDraggableState) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                super.onPostScroll(consumed, available, source)

                if (consumed.y <= 0 && available.y > DRAWER_SWIPE_DOWN_THRESHOLD_PIXELS) {
                    anchoredDraggableState.dispatchRawDelta(available.y)

                    // This runs when the user swipes down

                    // Caching the job ensures tonnes of jobs which closes the drawer doesn't get spawned,
                    // which would lead to janky UX and decreased performance.
                    if (job == null || !(job?.isActive ?: false)) {
                        job = scope.launch {
                            // Makes animations feel snappier when users have already swiped more than halfway.
                            if (anchoredDraggableState.requireOffset() > expandedHeight / 2) {
                                delay(250.milliseconds)
                            }
                            anchoredDraggableState.settle(
                                animationSpec = tween()
                            )
                        }

                        // Ensures a new job gets launched when the user swipes down later
                        job?.invokeOnCompletion {
                            job = null
                        }
                    }
                }

                return Offset.Zero
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
        Box(modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues(LocalDensity.current))) {
            AnimatedVisibility(!clockVisible, enter = fadeIn(), exit = fadeOut()) {
                Clock(time, palette)
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .offset {
                IntOffset(
                    x = 0, y = anchoredDraggableState.requireOffset().roundToInt()
                )
            }
            .nestedScroll(nestedScrollConnection)) {
            DrawerScreen(appItemPositionTracker)
        }
    }
}