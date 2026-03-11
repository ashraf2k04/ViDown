package com.ashraf.vidown.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ashraf.vidown.data.model.DownloadEntity

@Database(
    entities = [DownloadEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DownloadDatabase : RoomDatabase() {
    abstract fun downloadsDao(): DownloadDao
}