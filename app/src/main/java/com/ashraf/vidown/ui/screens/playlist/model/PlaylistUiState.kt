package com.ashraf.vidown.ui.screens.playlist.model

data class PlaylistUiState(
    val url: String? = null,

    val title: String = "",
    val items: List<PlaylistItemUi> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedCount: Int = 0,
    val canDownloadSelected: Boolean = false
)
