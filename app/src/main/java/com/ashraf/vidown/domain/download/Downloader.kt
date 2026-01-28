package com.ashraf.vidown.domain.download

import android.content.Context
import android.util.Log
import com.ashraf.vidown.notification.DOWNLOAD_NOTIFICATION_ID_BASE
import com.ashraf.vidown.notification.NotificationNotifier
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Downloader @Inject constructor(
    @ApplicationContext private val context : Context,
    private val notifier: NotificationNotifier,
    private val externalScope: CoroutineScope,
) {

    fun fetchAndDownload(
        url: String,
        outputDir: String,
        taskId: String,
    ) {
        val notificationId =
            DOWNLOAD_NOTIFICATION_ID_BASE + taskId.hashCode()

        Log.d("FLOW", "fetchAndDownload started: taskId=$taskId url=$url")

        externalScope.launch(Dispatchers.IO) {

            Log.d("FLOW", "Fetching info…")
            Log.d(
                "FLOW",
                "yt-dlp version = ${YoutubeDL.version(context)}"
            )

            notifier.showProgress(
                notificationId,
                title = "Preparing download",
                progress = 0,
                text = "Fetching video info…"
            )

            val infoResult: Result<VideoInfo> =
                DownloadUtil.fetchVideoInfoFromUrl(
                    url = url,
                    taskKey = taskId
                )

            infoResult
                .onSuccess { info: VideoInfo ->

                    Log.d("FLOW", "Info fetched: title=${info.title}")

                    notifier.showProgress(
                        notificationId,
                        title = "Downloading",
                        text = info.title,
                        progress = 0
                    )

                    val downloadResult: Result<List<String>> =
                        DownloadUtil.downloadVideo(
                            videoUrl = info.originalUrl!!,
                            taskId = taskId,
                            outputDir = outputDir,
                            progressCallback = { progress, _, text ->
                                Log.d("FLOW", "Progress=$progress text=$text")
                                notifier.showProgress(
                                    notificationId,
                                    title = info.title,
                                    progress = progress.toInt(),
                                    text = text
                                )
                            }
                        )

                    downloadResult
                        .onSuccess {
                            notifier.showCompleted(
                                notificationId,
                                title = "Download complete"
                            )
                            Log.d("FLOW", "Download completed")
                        }
                        .onFailure { error: Throwable ->
                            Log.e("FLOW", "Download failed", error)
                            notifier.showError(
                                notificationId,
                                title = "Download failed",
                                text = error.message ?: "Unknown error"
                            )
                        }
                }
                .onFailure { error: Throwable ->
                    Log.e("FLOW", "Fetch failed", error)
                    notifier.showError(
                        notificationId,
                        title = "Fetch failed",
                        text = error.message ?: "Unknown error"
                    )
                }
        }
    }
}