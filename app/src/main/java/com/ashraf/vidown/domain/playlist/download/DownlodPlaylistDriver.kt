package com.ashraf.vidown.domain.playlist.download

import android.util.Log
import com.ashraf.vidown.domain.helpers.PlaylistEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadPlaylistDriver @Inject constructor(
    private val serviceManager: ForegroundServiceManager,
    private val externalScope: CoroutineScope,
) {

    fun downloadPlaylist(
        title: String?,
        playlist: List<PlaylistEntry>,
        outputDir: String,
        formatSelector: String,
        taskPrefix: String,
    ) {

        serviceManager.start()

        PlaylistStateManager.insertPending(
            playlist,
            taskPrefix
        )

        externalScope.launch(Dispatchers.IO) {

            val service =
                serviceManager.awaitService() ?: return@launch

            val result =
                DownloadPlaylistEngine.downloadPlaylist(
                    title = title,
                    playlist = playlist,
                    outputDir = outputDir,
                    formatSelector = formatSelector,
                    taskPrefix = taskPrefix,
                    progressCallback = { videoName, progress ->

                        val index =
                            playlist.indexOfFirst {
                                (it.title ?: it.id) == videoName
                            }

                        if (index >= 0) {
                            val taskId = "${taskPrefix}_$index"

                            PlaylistStateManager.updateProgress(
                                taskId,
                                progress
                            )

                            service.updateProgress(
                                progress.toInt(),
                                videoName
                            )
                        }
                    }
                )

            result
                .onFailure {
                    Log.e("DL_FLOW", "Playlist failed: ${it.message}")
                }
        }
    }

    fun cancel(task: String) {
        PlaylistStateManager.cancelTask(taskId = task)
    }
}