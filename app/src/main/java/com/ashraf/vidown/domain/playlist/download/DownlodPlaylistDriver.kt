package com.ashraf.vidown.domain.playlist.download

import android.util.Log
import com.ashraf.vidown.domain.DownloadEngineImplement
import com.ashraf.vidown.database.DownloadEntity
import com.ashraf.vidown.database.DownloadRepository
import com.ashraf.vidown.domain.helpers.PlaylistEntry
import com.ashraf.vidown.domain.helpers.VideoInfo
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadPlaylistDriver @Inject constructor(
    private val serviceManager: ForegroundServiceManager,
    private val externalScope: CoroutineScope,
    private val repository: DownloadRepository,
    private val downloadEngineImplement: DownloadEngineImplement
) {

    fun downloadPlaylist(
        title: String?,
        playlist: List<PlaylistEntry>,
        outputDir: String,
        formatSelector: String,
        taskPrefix: String,
    ) {

        serviceManager.start()

        externalScope.launch(Dispatchers.IO) {

            val service =
                serviceManager.awaitService() ?: return@launch

            playlist.forEachIndexed { index, entry ->

                val taskId = "${taskPrefix}_$index"

                Log.d("URL", "${entry.thumbnails}")
                Log.d("URL", "${entry.thumbnails?.firstOrNull()?.url}")

                repository.insert(
                    DownloadEntity(
                        taskId = taskId,
                        title = entry.title!!,
                        filePath = outputDir,
                        imageUrl = entry.thumbnails?.firstOrNull()?.url,
                        playlistId = taskPrefix,
                        playlistTitle = title,
                        downloadedBytes = 0L,
                        totalBytes = null,
                        status = DownloadStatus.PENDING,
                        error = null,
                        createdAt = System.currentTimeMillis()
                    )
                )

            }

            playlist.forEachIndexed { index, entry ->

                val taskId = "${taskPrefix}_$index"

                val videoInfo = VideoInfo(
                    id = entry.id!!,
                    title = entry.title!!,
                    originalUrl = entry.url
                        ?: "https://www.youtube.com/watch?v=${entry.id}",
                    thumbnail = entry.thumbnails?.firstOrNull()?.url
                )

                downloadEngineImplement.downloadSuspend(
                    info = videoInfo,
                    formatSelector = formatSelector,
                    outputDir = "$outputDir/${title ?: "Playlist"}",
                    taskId = taskId,
                )
            }
        }
    }
}