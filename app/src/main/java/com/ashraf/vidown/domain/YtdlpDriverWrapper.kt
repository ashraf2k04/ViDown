package com.ashraf.vidown.domain

import android.util.Log
import com.ashraf.vidown.database.DownloadRepository
import com.ashraf.vidown.domain.video.download.DownloadVideoDriver
import com.ashraf.vidown.domain.playlist.download.DownloadPlaylistDriver
import com.ashraf.vidown.domain.playlist.fetch.FetchPlaylistInfoDriver
import com.ashraf.vidown.domain.video.fetch.FetchVideoInfoDriver
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YtdlpDriverWrapper @Inject constructor(
    val downloadVideoDriver: DownloadVideoDriver,
    val fetchVideoInfoDriver: FetchVideoInfoDriver,
    val downloadPlaylistDriver : DownloadPlaylistDriver,
    val fetchPlaylistInfoDriver: FetchPlaylistInfoDriver,
    private val externalScope: CoroutineScope,
    private val repository: DownloadRepository
){
    fun cancel(taskId: String) {
        Log.d("TASK", taskId)
        externalScope.launch {
            YoutubeDL.getInstance().destroyProcessById(taskId)
        }
    }
}