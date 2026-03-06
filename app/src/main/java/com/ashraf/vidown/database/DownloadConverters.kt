package com.ashraf.vidown.database

import androidx.room.TypeConverter
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus

class DownloadConverters {

    @TypeConverter
    fun fromStatus(status: DownloadStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): DownloadStatus =
        DownloadStatus.valueOf(value)
}
