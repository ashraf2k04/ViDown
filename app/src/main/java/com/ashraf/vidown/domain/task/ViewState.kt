package com.ashraf.vidown.domain.task


import com.ashraf.vidown.domain.download.Format
import com.ashraf.vidown.domain.download.VideoInfo
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class ViewState(
    val url: String = "",
    val title: String = "",
    val uploader: String = "",
    val extractorKey: String = "",
    val duration: Int = 0,
    val fileSizeApprox: Double = .0,
    val thumbnailUrl: String? = null,
    val videoFormats: List<Format>? = null,
    val audioOnlyFormats: List<Format>? = null,
) {
    companion object {
        fun fromVideoInfo(info: VideoInfo): ViewState {
            val formats =
                info.requestedFormats
                    ?: info.requestedDownloads?.map { it.toFormat() }
                    ?: emptyList()

            return ViewState(
                url = info.originalUrl.toString(),
                title = info.title,
                uploader = info.uploader ?: info.channel ?: info.uploaderId.toString(),
                extractorKey = info.extractorKey,
                duration = info.duration?.roundToInt() ?: 0,
                thumbnailUrl = info.thumbnail.toHttpsUrl(),
                fileSizeApprox = info.fileSize ?: info.fileSizeApprox ?: .0,
                videoFormats = formats.filter { it.containsVideo() },
                audioOnlyFormats = formats.filter { it.isAudioOnly() },
            )
        }
    }
}

private fun String?.toHttpsUrl(): String? {
    if (this.isNullOrBlank()) return null


    return when {
        startsWith("https://") -> this
        startsWith("http://") -> replaceFirst("http://", "https://")
        else -> this
    }
}
