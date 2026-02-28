package com.ashraf.vidown.ui.screens.downloads.helpers

import com.ashraf.vidown.ui.screens.homescreen.helpers.formatBytes

data class DownloadUiItem(
    val taskId: String,
    val title: String,
    val progress: Float,           // 0f..1f
    val downloadedBytes: Long,
    val totalBytes: Long?,         // nullable (estimate)
    val status: DownloadStatus,
    val error: String? = null
)


fun DownloadUiItem.progressText(): String {
    return if (totalBytes != null)
        "${formatBytes(downloadedBytes)} / ~${formatBytes(totalBytes)}"
    else
        formatBytes(downloadedBytes)
}

fun formatBytes(bytes: Long): String {
    val mb = bytes / (1024f * 1024f)
    return String.format("%.1f MB", mb)
}

