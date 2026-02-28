package com.ashraf.vidown.domain

import com.ashraf.vidown.domain.video.download.DownloadVideoDriver
import com.ashraf.vidown.domain.playlist.download.DownloadPlaylistDriver
import com.ashraf.vidown.domain.playlist.fetch.FetchPlaylistInfoDriver
import com.ashraf.vidown.domain.video.fetch.FetchVideoInfoDriver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YtdlpDriverWrapper @Inject constructor(
    val downloadVideoDriver: DownloadVideoDriver,
    val fetchVideoInfoDriver: FetchVideoInfoDriver,
    val downloadPlaylistDriver : DownloadPlaylistDriver,
    val fetchPlaylistInfoDriver: FetchPlaylistInfoDriver,
)