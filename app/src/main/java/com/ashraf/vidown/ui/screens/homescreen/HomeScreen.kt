package com.ashraf.vidown.ui.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ashraf.vidown.ui.screens.homescreen.components.DownloadButton
import com.ashraf.vidown.ui.screens.homescreen.components.SearchBar
import com.ashraf.vidown.ui.screens.homescreen.components.SelectPropertyBottomSheet
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

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
            selectedQuality = state.selectedQuality,
            onQualitySelected = viewModel::onQualitySelected,
            onDownload = viewModel::onDownloadConfirmed,
            onDismiss = viewModel::dismissQualitySheet
        )
    }
}