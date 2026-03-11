package com.ashraf.vidown.domain.video.fetch

import androidx.annotation.CheckResult
import com.ashraf.vidown.domain.constants.BASENAME
import com.ashraf.vidown.domain.model.VideoInfo
import com.ashraf.vidown.domain.constants.json
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FetchVideoInfoEngine {
    @CheckResult
    suspend fun fetchVideoInfo(
        url: String,
        playlistIndex: Int? = null,
        taskKey: String,
    ): Result<VideoInfo> = withContext(Dispatchers.IO) {
        runCatching {
            val request =
                YoutubeDLRequest(url).apply {
                    addOption("-o", BASENAME)
                    addOption("--dump-single-json")
                    addOption("--no-playlist")
                    addOption("-R", "1")
                    addOption("--socket-timeout", "10")

                    if (playlistIndex != null) {
                        addOption("--playlist-items", playlistIndex)
                    }
                }

            val response =
                YoutubeDL.getInstance().execute(request, taskKey, null)

            json.decodeFromString<VideoInfo>(response.out)
        }
    }
}