package com.ashraf.vidown.domain.video.download

import android.util.Log
import androidx.annotation.CheckResult
import com.ashraf.vidown.domain.helpers.OUTPUT_TEMPLATE_DEFAULT
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DownloadVideoEngine {

    @CheckResult
    suspend fun downloadVideo(
        videoUrl: String,
        taskId: String,
        outputDir: String,
        formatSelector: String,
        outputTemplate: String = OUTPUT_TEMPLATE_DEFAULT,
        progressCallback: ((Float, Long, String) -> Unit)? = null,
    ): Result<List<String>> = withContext(Dispatchers.IO) {
        runCatching {

            val request = YoutubeDLRequest(videoUrl).apply {

                addOption("--no-mtime")
                addOption("--no-playlist")
                addOption("-P", outputDir)
                addOption("-o", outputTemplate)

                // 🎯 FORMAT COMES FROM UI SELECTION
                addOption("-f", formatSelector)

                addOption("--merge-output-format", "mp4")
                //addOption("--downloader", "libaria2c.so")

                addOption("--socket-timeout", "10")
                addOption("--retries", "2")

                addOption(
                    "--extractor-args",
                    "youtube:player_client=android,web,tv"
                )
                addOption("--progress")
                addOption("--newline")
            }

            Log.d("DOWNLOAD", request.toString())

            val response =
                YoutubeDL.getInstance()
                    .execute(request, taskId, progressCallback)

            response.out.lines().filter {
                it.endsWith(".mp4") ||
                        it.endsWith(".mkv") ||
                        it.endsWith(".webm") ||
                        it.endsWith(".mp3") ||
                        it.endsWith(".m4a")
            }
        }
    }
}