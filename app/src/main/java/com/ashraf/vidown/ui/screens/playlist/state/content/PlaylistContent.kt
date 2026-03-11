package com.ashraf.vidown.ui.screens.playlist.state.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ashraf.vidown.ui.screens.playlist.PlaylistViewModel
import com.ashraf.vidown.ui.screens.playlist.model.PlaylistUiState
import com.ashraf.vidown.ui.screens.playlist.state.content.components.ContentBottomBar
import com.ashraf.vidown.ui.screens.playlist.state.content.components.ContentHeader
import com.ashraf.vidown.ui.screens.playlist.state.content.components.ContentList

@Composable
fun PlaylistContent(
    state: PlaylistUiState,
    viewModel: PlaylistViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        ContentHeader(
            title = state.title,
            totalCount = state.items.size,
            selectedCount = state.selectedCount
        )

        ContentList(
            modifier = Modifier.weight(1f),
            state = state,
            viewModel = viewModel
        )

        ContentBottomBar(
            selectedCount = state.selectedCount,
            enableSelected = state.canDownloadSelected,
            onDownloadSelected = viewModel::onDownloadSelected,
            onDownloadAll = viewModel::onDownloadAll
        )
    }
}