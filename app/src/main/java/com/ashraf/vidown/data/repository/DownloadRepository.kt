package com.ashraf.vidown.data.repository

import android.content.Context
import androidx.room.withTransaction
import coil.imageLoader
import coil.memory.MemoryCache
import com.ashraf.vidown.data.database.DownloadDao
import com.ashraf.vidown.data.database.DownloadDatabase
import com.ashraf.vidown.data.model.DownloadEntity
import com.ashraf.vidown.ui.screens.downloads.model.DownloadStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(
    private val database: DownloadDatabase,
    private val downloadDao: DownloadDao,
    @ApplicationContext private val context: Context
) {

    // ----------------------------
    // OBSERVE
    // ----------------------------

    suspend fun getById(id: String): DownloadEntity {
        return downloadDao.getById(id)
    }

     fun observeAll(): Flow<List<DownloadEntity>> =
        downloadDao.observeAll()

    fun observeByStatus(status: DownloadStatus): Flow<List<DownloadEntity>> =
        downloadDao.observeByStatus(status)

    fun observeByPlaylist(playlistId: String): Flow<List<DownloadEntity>> =
        downloadDao.observeByPlaylist(playlistId)


    // ----------------------------
    // INSERT
    // ----------------------------

    suspend fun insert(download: DownloadEntity) {
        downloadDao.insert(download)
    }

    suspend fun insertAll(downloads: List<DownloadEntity>) {
        downloadDao.insertAll(downloads)
    }


    // ----------------------------
    // UPDATE
    // ----------------------------

    suspend fun updateProgress(
        taskId: String,
        downloadedBytes: Long,
        totalBytes: Long?,
        status: DownloadStatus,
        error : String? = null
    ) {
        downloadDao.updateProgress(
            taskId = taskId,
            downloadedBytes = downloadedBytes,
            totalBytes = totalBytes,
            status = status
        )
    }

    suspend fun updateStatus(
        taskId: String,
        status: DownloadStatus,
        error: String? = null
    ) {
        downloadDao.updateStatus(taskId, status, error)
    }

    // ----------------------------
    // DELETE - HARD (file + db + cache)
    // ----------------------------

    suspend fun hardDelete(taskId: String) {
        database.withTransaction {
            downloadDao.hardDelete(taskId)
        }
    }

    suspend fun hardDeleteSelected(ids: List<String>) {
        database.withTransaction {
            downloadDao.hardDeleteSelected(ids)
        }
    }

    suspend fun deleteAll() {
        database.withTransaction {
            downloadDao.deleteAll()
        }
    }

    // ----------------------------
    // DELETE - SOFT
    // ----------------------------

    suspend fun softDelete(taskId: String) {
        downloadDao.softDelete(taskId)
    }

    suspend fun softDeleteSelected(ids: List<String>) {
        downloadDao.softDeleteSelected(ids)
    }


    // ----------------------------
    // FILE + CACHE CLEANUP VERSION
    // ----------------------------

    suspend fun deleteWithFileAndCache(download: DownloadEntity) {
        database.withTransaction {

            // 1️⃣ Delete physical file
            try {
                val file = File(download.filePath)
                if (file.exists()) file.delete()
            } catch (_: Exception) {
                // log if needed
            }

            // 2️⃣ Remove image from Coil cache
            download.imageUrl?.let { url ->
                val loader = context.imageLoader

                loader.diskCache?.remove(url)

                loader.memoryCache?.remove(
                    MemoryCache.Key(url)
                )
            }

            // 3️⃣ Remove from DB
            downloadDao.hardDelete(download.taskId)
        }
    }

    suspend fun deleteSelectedWithFiles(downloads: List<DownloadEntity>) {
        database.withTransaction {
            downloads.forEach { deleteWithFileAndCache(it) }
        }
    }


    // ----------------------------
    // RECOVERY
    // ----------------------------

    suspend fun recoverIncompleteDownloads() {
        downloadDao.markIncompleteAsFailed()
    }
}