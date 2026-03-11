package com.ashraf.vidown.ui.screens.playlist.mapper

fun isPlaylist(url: String): Boolean =
    url.contains("playlist") || url.contains("list=")
