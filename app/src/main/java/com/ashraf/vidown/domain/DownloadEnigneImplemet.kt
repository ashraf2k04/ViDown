package com.ashraf.vidown.domain

import android.util.Log
import com.ashraf.vidown.database.DownloadRepository
import com.ashraf.vidown.domain.helpers.VideoInfo
import com.ashraf.vidown.domain.video.download.DownloadServiceController
import com.ashraf.vidown.domain.video.download.DownloadVideoEngine
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class DownloadEngineImplement @Inject constructor(
    private val serviceController: DownloadServiceController,
    private val externalScope: CoroutineScope,
    private val repository: DownloadRepository
) {

    suspend fun downloadSuspend(
        info: VideoInfo,
        formatSelector: String,
        outputDir: String,
        taskId: String,
    ): Result<List<String>>{

        File(outputDir).mkdirs()

        val service = serviceController.ensureServiceReady()
            ?: return Result.failure(
                IllegalStateException("Service not ready")
            )

        var lastDbUpdateTime = 0L

        val result = DownloadVideoEngine.downloadVideo(
            videoUrl = info.originalUrl
                ?: return Result.failure(
                    IllegalStateException("URL null")
                ),
            taskId = taskId,
            outputDir = outputDir,
            formatSelector = formatSelector,
            progressCallback = { progress, bytes, text ->

                val parsed = parseProgress(text)
                val downloaded = parsed?.downloadedBytes ?: bytes
                val total = parsed?.totalBytes

                service.updateProgress(progress.toInt(), text)

                Log.d("RESPONSE", parsed.toString())
                Log.d("RESPONSE", parsed?.downloadedBytes.toString())
                Log.d("RESPONSE", parsed?.totalBytes.toString())
                Log.d("RESPONSE", parsed?.etaSeconds.toString())
                Log.d("RESPONSE", text)

                val now = System.currentTimeMillis()
                if (now - lastDbUpdateTime > 1500) {

                    externalScope.launch{
                        repository.updateProgress(
                            taskId = taskId,
                            downloadedBytes = downloaded,
                            totalBytes = total,
                            status = DownloadStatus.DOWNLOADING
                        )
                    }
                    lastDbUpdateTime = now
                }
            }
        )

        result.onSuccess {

            externalScope.launch {
                repository.updateStatus(
                    taskId,
                    DownloadStatus.COMPLETED
                )
            }
            service.complete(info.title)

        }.onFailure { error ->

            Log.d("TASK", "${error.message}")

            if (error.message == null) {
                externalScope.launch {
                    repository.updateStatus(
                        taskId,
                        DownloadStatus.CANCELED,
                        "Cancelled"
                    )
                }
            } else {
                externalScope.launch {
                    repository.updateStatus(
                        taskId,
                        DownloadStatus.FAILED,
                        error.message
                    )
                }
            }

            service.error(error.message ?: "Download failed")
        }

        return result
    }

    data class DownloadProgress(
        val percent: Float,
        val totalBytes: Long?,
        val speedBytesPerSec: Long?,
        val downloadedBytes : Long?,
        val etaSeconds: Int?
    )

    fun convertToBytes(value: Double, unit: String): Long {

        return when (unit) {
            "KiB" -> (value * 1024).toLong()
            "MiB" -> (value * 1024 * 1024).toLong()
            "GiB" -> (value * 1024 * 1024 * 1024).toLong()
            else -> value.toLong()
        }
    }

    fun parseProgress(text: String): DownloadProgress? {

        val regex = Regex(
            """(\d+\.\d+)%\s+of\s+([\d.]+)(KiB|MiB|GiB)\s+at\s+([\d.]+)(KiB|MiB|GiB)/s\s+ETA\s+(\d+):(\d+)"""
        )

        val match = regex.find(text) ?: return null

        val percent = match.groupValues[1].toFloat()

        val sizeValue = match.groupValues[2].toDouble()
        val sizeUnit = match.groupValues[3]

        val speedValue = match.groupValues[4].toDouble()
        val speedUnit = match.groupValues[5]

        val etaMin = match.groupValues[6].toInt()
        val etaSec = match.groupValues[7].toInt()

        val totalBytes = convertToBytes(sizeValue, sizeUnit)
        val speedBytes = convertToBytes(speedValue, speedUnit)
        val downloadedBytes = (totalBytes * percent) / 100

        return DownloadProgress(
            percent = percent,
            totalBytes = totalBytes,
            downloadedBytes = downloadedBytes.toLong(),
            speedBytesPerSec = speedBytes,
            etaSeconds = etaMin * 60 + etaSec
        )
    }
}