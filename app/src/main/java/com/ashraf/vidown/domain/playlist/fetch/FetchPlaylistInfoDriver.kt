package com.ashraf.vidown.domain.playlist.fetch

import com.ashraf.vidown.domain.helpers.PlaylistResult
import com.yausername.youtubedl_android.YoutubeDL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchPlaylistInfoDriver @Inject constructor() {

    private var currentTaskKey: String? = null

    suspend fun fetchPlaylist(
        url: String,
        taskKey : String
    ): Result<PlaylistResult> {

        currentTaskKey = taskKey

        return FetchPlaylistInfoEngine.fetchPlaylist(
            url = url,
            taskKey = taskKey
        )
    }

    fun cancel() {
        currentTaskKey?.let {
            YoutubeDL.getInstance().destroyProcessById(it)
        }
    }
}