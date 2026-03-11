package com.ashraf.vidown.data.database

import androidx.room.TypeConverter
import com.ashraf.vidown.ui.screens.downloads.model.DownloadStatus

class DownloadConverters {

    @TypeConverter
    fun fromStatus(status: DownloadStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): DownloadStatus =
        DownloadStatus.valueOf(value)
}
