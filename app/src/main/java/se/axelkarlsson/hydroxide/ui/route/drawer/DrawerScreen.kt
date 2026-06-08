package se.axelkarlsson.hydroxide.ui.route.drawer

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.axelkarlsson.hydroxide.ui.component.AppItem

@Composable
fun DrawerScreen(
    viewModel: DrawerViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.load()
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
                    viewModel.onAppItemTap(it)
                }, onLongClick = {
                    viewModel.onAppItemLongTap(it)
                })
            }
        }
    }
}