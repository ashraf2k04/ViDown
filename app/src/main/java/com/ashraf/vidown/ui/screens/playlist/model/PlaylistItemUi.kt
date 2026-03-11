package com.ashraf.vidown.ui.screens.playlist.model

import com.ashraf.vidown.domain.model.Thumbnail

data class PlaylistItemUi(
    val id: String?,
    val title: String?,
    val url: String?,
    val duration: String? = null,
    val isSelected: Boolean = false,
    val thumbnails : List<Thumbnail>? = emptyList()
)
