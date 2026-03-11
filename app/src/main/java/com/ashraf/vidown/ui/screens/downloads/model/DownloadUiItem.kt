package com.ashraf.vidown.ui.screens.downloads.model


data class DownloadUiItem(
    val taskId: String,
    val title: String,
    val progress: Float,           // 0f..1f
    val downloadedBytes: Long,
    val totalBytes: Long?,         // nullable (estimate)
    val status: DownloadStatus,
    val error: String? = null,
    val playlistId: String? = null,
    val playlistTitle: String? = null,
    val imageUrl : String? = null,
    val filepath : String,
    val createdAt : Long
)


