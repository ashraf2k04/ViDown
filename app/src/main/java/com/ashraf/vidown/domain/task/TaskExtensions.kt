package com.ashraf.vidown.domain.task

import android.content.Context
import android.os.Environment
import com.ashraf.vidown.domain.DownloadEngineImpl
import com.ashraf.vidown.domain.DownloadState
import com.ashraf.vidown.domain.RestartableAction
import com.ashraf.vidown.domain.download.VideoInfo
import com.ashraf.vidown.domain.download.*
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//internal fun Task.prepare(downloader: DownloaderV2Impl) {
//    check(downloader.getTaskStateMap()[this]?.downloadState == DownloadState.Idle)
//    fetchInfo(downloader)
//}

internal fun Task.prepare(downloader: DownloadEngineImpl) {
    val stateMap = downloader.getTaskStateMap()
    val state = stateMap[this] ?: return

    check(state.downloadState is DownloadState.Idle)

    fetchInfo(downloader)
}

internal fun Task.fetchInfo(downloader: DownloadEngineImpl) {
    val stateMap = downloader.getTaskStateMap()
    val scope = downloader.scope

    val job =
        scope.launch(Dispatchers.IO) {
            val result: Result<VideoInfo> =
                DownloadUtil.fetchVideoInfoFromUrl(
                    url = url,
                    playlistIndex = (type as? TypeInfo.Playlist)?.index,
                    taskKey = id,
                )

            result
                .onSuccess { info ->
                    val prev = stateMap[this@fetchInfo] ?: return@onSuccess
                    stateMap[this@fetchInfo] =
                        prev.copy(
                            downloadState = DownloadState.ReadyWithInfo,
                            videoInfo = info,
                            viewState = ViewState.fromVideoInfo(info),
                        )
                }
                .onFailure { throwable ->
                    if (throwable is YoutubeDL.CanceledException) return@onFailure

                    val prev = stateMap[this@fetchInfo] ?: return@onFailure
                    stateMap[this@fetchInfo] =
                        prev.copy(
                            downloadState =
                                DownloadState.Error(
                                    throwable = throwable,
                                    action = RestartableAction.FetchInfo
                                )
                        )
                }
        }

    // update state ONCE, atomically
    stateMap[this] =
        stateMap[this]!!.copy(
            downloadState = DownloadState.FetchingInfo(job = job, taskId = id)
        )
}

internal fun Task.download(
    downloader: DownloadEngineImpl,
    context: Context,
) {
    val outputDir =
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .absolutePath

    val stateMap = downloader.getTaskStateMap()
    val prev = stateMap[this] ?: return
    val info = prev.videoInfo ?: return

    val job =
        downloader.scope.launch {
            DownloadUtil.downloadVideo(
                videoUrl = info.originalUrl!!,
                taskId = id,
                outputDir = outputDir,
            ).onSuccess { files ->
                stateMap[this@download] =
                    prev.copy(
                        downloadState =
                            DownloadState.Completed(
                                filePath = files.firstOrNull()
                            )
                    )
            }.onFailure { error ->
                if (error is YoutubeDL.CanceledException) return@onFailure

                stateMap[this@download] =
                    prev.copy(
                        downloadState =
                            DownloadState.Error(
                                throwable = error,
                                action = RestartableAction.Download
                            )
                    )
            }
        }

    stateMap[this] =
        prev.copy(
            downloadState =
                DownloadState.Running(
                    job = job,
                    taskId = id
                )
        )
}

/* ------------------------- */
/* CANCEL                    */
/* ------------------------- */

internal fun Task.cancelImpl(
    map: MutableMap<Task, Task.State>
): Boolean {
    val state = map[this]?.downloadState ?: return false

    if (state is DownloadState.Cancelable) {
        YoutubeDL.destroyProcessById(state.taskId)
        state.job.cancel()

        map[this] =
            map[this]!!.copy(
                downloadState = DownloadState.Canceled(state.action)
            )
        return true
    }
    return false
}

/* ------------------------- */
/* RESTART                   */
/* ------------------------- */

internal fun Task.restartImpl(
    map: MutableMap<Task, Task.State>
) {
    val state = map[this]?.downloadState ?: return

    if (state is DownloadState.Restartable) {
        map[this] =
            map[this]!!.copy(
                downloadState =
                    when (state.action) {
                        RestartableAction.FetchInfo -> DownloadState.Idle
                        RestartableAction.Download -> DownloadState.ReadyWithInfo
                    }
            )
    }
}