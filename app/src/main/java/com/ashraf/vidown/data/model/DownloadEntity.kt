package com.ashraf.vidown.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ashraf.vidown.data.database.DownloadConverters
import com.ashraf.vidown.ui.screens.downloads.model.DownloadStatus

@Entity(
    tableName = "downloads",
    indices = [
        Index(value = ["status"]),
        Index(value = ["playlist_id"]),
        Index(value = ["created_at"])
    ]
)
@TypeConverters(DownloadConverters::class)
data class DownloadEntity(

    @PrimaryKey
    @ColumnInfo(name = "task_id")
    val taskId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "file_path")
    val filePath: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    @ColumnInfo(name = "playlist_id")
    val playlistId: String? = null,

    @ColumnInfo(name = "playlist_title")
    val playlistTitle: String? = null,

    @ColumnInfo(name = "downloaded_bytes", defaultValue = "0")
    val downloadedBytes: Long = 0L,

    @ColumnInfo(name = "total_bytes")
    val totalBytes: Long? = null,

    @ColumnInfo(name = "status")
    val status: DownloadStatus,

    @ColumnInfo(name = "error_message")
    val error: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "is_deleted", defaultValue = "0")
    val isDeleted: Boolean = false,

    @ColumnInfo(name = "etag")
    val etag: String? = null,

    @ColumnInfo(name = "last_modified")
    val lastModified: String? = null
)