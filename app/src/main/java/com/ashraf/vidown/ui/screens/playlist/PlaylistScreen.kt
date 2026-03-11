package com.ashraf.vidown.ui.screens.playlist

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ashraf.vidown.ui.navigation.Routes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.ashraf.vidown.ui.screens.playlist.state.error.PlaylistError
import com.ashraf.vidown.ui.screens.playlist.state.content.PlaylistContent
import com.ashraf.vidown.ui.screens.playlist.state.loading.PlaylistLoading
import com.ashraf.vidown.ui.screens.playlist.model.PlaylistEvent

@Composable
fun PlaylistScreen(
    url: String,
    navController: NavController,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(url)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PlaylistEvent.NavigateToDownloads ->
                    navController.navigate(Routes.DownloadsScreen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
            }
        }
    }

    when {
        state.isLoading -> PlaylistLoading()
        state.error != null -> PlaylistError(
            error = state.error!!,
            onRetry = { viewModel.init(url) }
        )
        else -> PlaylistContent(state, viewModel)
    }
}