package com.ashraf.vidown.domain.playlist.download

import com.ashraf.vidown.domain.helpers.PlaylistEntry
import com.ashraf.vidown.domain.video.download.DownloadVideoEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DownloadPlaylistEngine {
    suspend fun downloadPlaylist(
        title : String?,
        playlist:  List<PlaylistEntry>,
        outputDir: String,
        formatSelector: String,
        taskPrefix: String,
        progressCallback: ((String, Float) -> Unit)? = null
    ): Result<Unit> {
        val withContext = withContext<Result<Unit>>(Dispatchers.IO) {

            runCatching {

                playlist.forEachIndexed { index, entry ->

                    val videoUrl =
                        entry.url ?: "https://www.youtube.com/watch?v=${entry.id}"

                    val taskId = "${taskPrefix}_$index"

                    val videoName =
                        entry.title ?: entry.id ?: "Video_${index + 1}"

                    //DownloadStateBus.markPending(taskId)

                    DownloadVideoEngine.downloadVideo(
                        videoUrl = videoUrl,
                        taskId = taskId,
                        outputDir = "$outputDir/${title ?: "Playlist"}",
                        formatSelector = formatSelector,
                        progressCallback = { progress, _, _ ->
                            val videoName =
                                entry.title
                                    ?: entry.id
                                    ?: "Video_${index + 1}"

                            progressCallback?.invoke(videoName, progress)
                        }
                    ).getOrThrow()
                }
            }
        }
        return withContext
    }
}