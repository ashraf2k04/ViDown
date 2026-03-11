package com.ashraf.vidown.ui.screens.playlist.mapper

import com.ashraf.vidown.domain.model.PlaylistEntry
import com.ashraf.vidown.ui.screens.playlist.model.PlaylistItemUi

fun PlaylistItemUi.toPlaylistDomainMapper(): PlaylistEntry =
    PlaylistEntry(
        id = id,
        title = title,
        url = url,
        thumbnails = thumbnails
    )
