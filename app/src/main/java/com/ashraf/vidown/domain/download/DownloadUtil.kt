package com.ashraf.vidown.domain.download

import androidx.annotation.CheckResult
import com.yausername.youtubedl_android.*
import kotlinx.serialization.json.Json

object DownloadUtil {

    private val json = Json { ignoreUnknownKeys = true }

    /* ----------------------------- */
    /* -------- TEMPLATES ---------- */
    /* ----------------------------- */

    private const val BASENAME = "%(title).200B"
    private const val EXTENSION = ".%(ext)s"

    const val OUTPUT_TEMPLATE_DEFAULT = BASENAME + EXTENSION

    /* ----------------------------- */
    /* -------- FETCH INFO --------- */
    /* ----------------------------- */

    @CheckResult
    fun fetchVideoInfoFromUrl(
        url: String,
        playlistIndex: Int? = null,
        taskKey: String,
    ): Result<VideoInfo> =
        runCatching {
            val request =
                YoutubeDLRequest(url).apply {
                    addOption("-o", BASENAME)
                    addOption("--dump-single-json")
                    addOption("--no-playlist")
                    addOption("-R", "1")
                    addOption("--socket-timeout", "10")

                    if (playlistIndex != null) {
                        addOption("--playlist-items", playlistIndex)
                    }
                }

            val response =
                YoutubeDL.getInstance().execute(request, taskKey, null)

            json.decodeFromString<VideoInfo>(response.out)
        }

    /* ----------------------------- */
    /* -------- DOWNLOAD ----------- */
    /* ----------------------------- */

    @CheckResult
    fun downloadVideo(
        videoUrl: String,
        taskId: String,
        outputDir: String,
        outputTemplate: String = OUTPUT_TEMPLATE_DEFAULT,
        progressCallback: ((Float, Long, String) -> Unit)? = null,
    ): Result<List<String>> =
        runCatching {
            val request = YoutubeDLRequest(videoUrl).apply {

                // ─── Output ─────────────────────────────
                addOption("--no-mtime")
                addOption("--no-playlist")
                addOption("-P", outputDir)
                addOption("-o", outputTemplate)


                addOption(
                    "-f",
                    "bestvideo[height<=480]/bestvideo[height<=360]/best[height<=480]/best"
                )
                addOption("--merge-output-format", "mp4")

                addOption("--downloader", "libaria2c.so")
                // ─── Network stability ──────────────────
                addOption("--socket-timeout", "10")
                addOption("--retries", "2")

                addOption(
                    "--extractor-args",
                    "youtube:player_client=android,web,tv"
                )
            }
            val response =
                YoutubeDL.getInstance()
                    .execute(request, taskId, progressCallback)

            //json.decodeFromString<VideoInfo>(response.out)

            response.out
                .lines()
                .filter {
                    it.endsWith(".mp4") ||
                            it.endsWith(".mkv") ||
                            it.endsWith(".webm") ||
                            it.endsWith(".mp3") ||
                            it.endsWith(".m4a")
                }


        }
}