package com.ashraf.vidown.domain.video.download

import android.util.Log
import com.ashraf.vidown.domain.helpers.VideoInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadVideoDriver @Inject constructor(
    private val serviceController: DownloadServiceController,
    private val externalScope: CoroutineScope
) {

    fun download(
        info: VideoInfo,
        formatSelector: String,
        outputDir: String,
        taskId: String,
    ) {

        File(outputDir).mkdirs()

        Log.d("DOWNLOAD", "Selector = $formatSelector")
        Log.d("DOWNLOAD", "Output dir exists = ${File(outputDir).exists()}")

        DownloadStateUpdater.insertInitial(taskId, info)

        externalScope.launch(Dispatchers.IO) {

            val service = serviceController.ensureServiceReady()

            if (service == null) {
                Log.e("DOWNLOAD", "Service not ready")
                return@launch
            }



            val result = DownloadVideoEngine.downloadVideo(
                videoUrl = info.originalUrl!!,
                taskId = taskId,
                outputDir = outputDir,
                formatSelector = formatSelector,
                progressCallback = { progress, bytes, text ->

                    DownloadStateUpdater.updateProgress(
                        taskId,
                        progress,
                        bytes
                    )

                    service.updateProgress(progress.toInt(), text)
                }
            )

            result
                .onSuccess {
                    DownloadStateUpdater.markCompleted(taskId)
                    service.complete(info.title)
                }
                .onFailure { error ->
                    DownloadStateUpdater.markFailed(
                        taskId,
                        error.message ?: "Failed"
                    )
                    Log.e("DOWNLOAD", "Download failed", error)
                    service.error(error.message ?: "Download failed")
                }
        }
    }

    fun cancel(taskId: String) {
        DownloadStateUpdater.markCancel(taskId)
    }
}