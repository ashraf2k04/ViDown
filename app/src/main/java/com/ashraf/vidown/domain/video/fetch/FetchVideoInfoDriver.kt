package com.ashraf.vidown.domain.video.fetch

import com.ashraf.vidown.domain.helpers.VideoInfo
import com.yausername.youtubedl_android.YoutubeDL
import javax.inject.Inject

class FetchVideoInfoDriver @Inject constructor() {

    suspend fun fetchVideoInfo(
        url: String,
        taskKey : String
    ): Result<VideoInfo> {
        return FetchVideoInfoEngine.fetchVideoInfo(
            url = url,
            taskKey = taskKey
        )
    }

    fun cancel(taskKey: String) {
        YoutubeDL.getInstance().destroyProcessById(taskKey)
    }
}