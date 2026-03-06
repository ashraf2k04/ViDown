package com.ashraf.vidown.ui.screens.downloads.helpers

sealed interface DownloadListItem {

    data class Single(
        val item: DownloadUiItem
    ) : DownloadListItem

    data class PlaylistGroup(
        val playlistId: String,
        val playlistTitle: String,
        val items: List<DownloadUiItem>,
        val isExpanded: Boolean
    ) : DownloadListItem
}