package com.ashraf.vidown.domain.playlist.fetch

import android.util.Log
import com.ashraf.vidown.domain.helpers.PlaylistResult
import com.ashraf.vidown.domain.helpers.json
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FetchPlaylistInfoEngine {

    suspend fun fetchPlaylist(
        url: String,
        taskKey: String,
    ): Result<PlaylistResult> = withContext(Dispatchers.IO) {
        runCatching {

            val request =
                YoutubeDLRequest(url).apply {
                    addOption("--dump-single-json")
                    addOption("--yes-playlist")
                    addOption("--flat-playlist")
                }

            val response =
                YoutubeDL.getInstance().execute(request, taskKey, null)

            Log.d("URL", response.out)
            Log.d("URL", response.err)
            Log.d("URL", response.elapsedTime.toString())
            Log.d("URL", response.command.toString())
            json.decodeFromString<PlaylistResult>(response.out)
        }
    }
}