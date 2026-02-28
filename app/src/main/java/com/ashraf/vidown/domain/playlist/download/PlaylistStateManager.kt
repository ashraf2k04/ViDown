package com.ashraf.vidown.domain.playlist.download

import com.ashraf.vidown.domain.helpers.PlaylistEntry
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStateBus
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadUiItem
import com.yausername.youtubedl_android.YoutubeDL

object PlaylistStateManager {

    fun insertPending(
        playlist: List<PlaylistEntry>,
        taskPrefix: String
    ) {
        playlist.forEachIndexed { index, entry ->
            val taskId = "${taskPrefix}_$index"
            val videoName =
                entry.title ?: entry.id ?: "Video_${index + 1}"

            DownloadStateBus.upsert(
                DownloadUiItem(
                    taskId = taskId,
                    title = videoName,
                    progress = 0f,
                    downloadedBytes = 0L,
                    totalBytes = null,
                    status = DownloadStatus.PENDING
                )
            )
        }
    }

    fun updateProgress(
        taskId: String,
        progress: Float
    ) {
        val current =
            DownloadStateBus.downloads.value
                .find { it.taskId == taskId } ?: return

        val normalized = (progress / 100f).coerceIn(0f, 1f)

        val newStatus =
            when {
                normalized >= 1f -> DownloadStatus.COMPLETED
                normalized > 0f  -> DownloadStatus.DOWNLOADING
                else             -> current.status
            }

        DownloadStateBus.upsert(
            current.copy(
                progress = normalized,
                status = newStatus
            )
        )
    }

    fun cancelTask(taskId: String){
        DownloadStateBus.downloads.value
            .filter { it.taskId.startsWith(taskId) }
            .forEach {
                YoutubeDL.getInstance().destroyProcessById(it.taskId)
                DownloadStateBus.markCanceled(it.taskId)
            }
    }
}