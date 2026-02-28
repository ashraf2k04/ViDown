package com.ashraf.vidown.domain.video.download

import com.ashraf.vidown.domain.helpers.VideoInfo
import com.ashraf.vidown.domain.helpers.estimateSize
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStateBus
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadUiItem
import com.yausername.youtubedl_android.YoutubeDL

object DownloadStateUpdater {

    fun insertInitial(taskId: String, info: VideoInfo) {
        DownloadStateBus.upsert(
            DownloadUiItem(
                taskId = taskId,
                title = info.title,
                progress = 0f,
                downloadedBytes = 0,
                totalBytes = info.estimateSize(),
                status = DownloadStatus.DOWNLOADING
            )
        )
    }

    fun updateProgress(
        taskId: String,
        progress: Float,
        downloadedBytes: Long
    ) {
        val current =
            DownloadStateBus.downloads.value
                .find { it.taskId == taskId } ?: return

        if (progress >= 0f) {
            val normalized = (progress / 100f).coerceIn(0f, 1f)

            DownloadStateBus.upsert(
                current.copy(
                    progress = normalized,
                    downloadedBytes = downloadedBytes,
                    status = DownloadStatus.DOWNLOADING
                )
            )
        }
    }

    fun markCompleted(taskId: String) =
        DownloadStateBus.markCompleted(taskId)

    fun markFailed(taskId: String, message: String) =
        DownloadStateBus.markFailed(taskId, message)

    fun markCancel(taskId: String) {
        YoutubeDL.getInstance().destroyProcessById(taskId)
        DownloadStateBus.markCanceled(taskId)
    }
}