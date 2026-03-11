package com.ashraf.vidown.ui.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ashraf.vidown.ui.screens.homescreen.components.DownloadButton
import com.ashraf.vidown.ui.screens.homescreen.components.SearchBar
import com.ashraf.vidown.ui.screens.homescreen.components.bottomsheet.SelectPropertyBottomSheet
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashraf.vidown.ui.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.OpenPlaylist -> {
                    navController.navigate(
                        Routes.PlaylistScreen.createRoute(event.url)
                    ){
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            SearchBar(
                value = state.url,
                onValueChange = viewModel::onUrlChange,
                onSearch = viewModel::onSearchOrDownloadClick
            )

            Spacer(Modifier.height(32.dp))

            DownloadButton(
                onClick = viewModel::onSearchOrDownloadClick
            )
        }

        SelectPropertyBottomSheet(
            show = state.showQualitySheet,
            state = state, // ✅ PASS THE ACTUAL STATE
            onQualitySelected = viewModel::onQualitySelected,
            onFormatSelected = viewModel::onFormatSelected,
            onAudioToggle = viewModel::onAudioToggle,
            onDownload = viewModel::onDownloadConfirmed,
            onDismiss = viewModel::dismissQualitySheet
        )
    }
}