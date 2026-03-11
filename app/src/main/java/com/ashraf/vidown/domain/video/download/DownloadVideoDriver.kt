package com.ashraf.vidown.domain.video.download

import com.ashraf.vidown.domain.engine.DownloadEngineImplement
import com.ashraf.vidown.data.model.DownloadEntity
import com.ashraf.vidown.data.repository.DownloadRepository
import com.ashraf.vidown.domain.model.VideoInfo
import com.ashraf.vidown.ui.screens.downloads.model.DownloadStatus
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadVideoDriver @Inject constructor(
    private val externalScope: CoroutineScope,
    private val repository: DownloadRepository,
    private val downloadEngineImplement: DownloadEngineImplement
) {

    fun download(
        playlistId: String? = null,
        playlistTitle: String? = null,
        info: VideoInfo,
        formatSelector: String,
        outputDir: String,
        taskId: String,
    ) {
        externalScope.launch(Dispatchers.IO) {

            if (playlistId == null) {
                repository.insert(
                    DownloadEntity(
                        taskId = taskId,
                        title = info.title,
                        filePath = outputDir,
                        imageUrl = info.thumbnail,
                        playlistId = playlistId,
                        playlistTitle = playlistTitle,
                        downloadedBytes = 0L,
                        totalBytes = null,
                        status = DownloadStatus.PENDING,
                        error = null,
                        createdAt = System.currentTimeMillis()
                    )
                )
            }

            downloadEngineImplement.downloadSuspend(
                info,
                formatSelector,
                outputDir,
                taskId
            )
        }
    }
}