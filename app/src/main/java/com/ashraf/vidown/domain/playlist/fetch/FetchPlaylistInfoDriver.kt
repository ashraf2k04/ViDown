package com.ashraf.vidown.domain.playlist.fetch

import com.ashraf.vidown.domain.model.PlaylistResult
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
}