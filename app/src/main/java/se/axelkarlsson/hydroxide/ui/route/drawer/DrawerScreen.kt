package se.axelkarlsson.hydroxide.ui.route.drawer

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import se.axelkarlsson.hydroxide.ui.component.AppItem
import se.axelkarlsson.hydroxide.ui.route.homescreen.HomeScreenDrawerAnchor

@Composable
fun DrawerScreen(
    anchoredDraggableState: AnchoredDraggableState<HomeScreenDrawerAnchor>,
    viewModel: DrawerViewModel = hiltViewModel()
) {
    val window = LocalActivity.current?.window
    val apps by viewModel.apps.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    // TODO: Refactor this code
    if (window != null) {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        if (anchoredDraggableState.currentValue == HomeScreenDrawerAnchor.EXPANDED) {
            insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        } else {
            insetsController.show(WindowInsetsCompat.Type.navigationBars())
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
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
                AppItem(it, onClick = {

                    // Resolves #6
                    scope.launch {
                        anchoredDraggableState.animateTo(HomeScreenDrawerAnchor.COLLAPSED)
                    }

                    viewModel.onAppItemTap(it)
                }, onLongClick = {
                    viewModel.onAppItemLongTap(it)
                })
            }
        }
    }
}