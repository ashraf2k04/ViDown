package com.ashraf.vidown.ui.screens.playlist.model

sealed interface PlaylistEvent {
    data object NavigateToDownloads : PlaylistEvent
}
