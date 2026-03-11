package com.ashraf.vidown.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashraf.vidown.data.model.DownloadEntity
import com.ashraf.vidown.ui.screens.downloads.model.DownloadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {

    @Query("SELECT * FROM downloads WHERE task_id = :id")
    suspend fun getById(id: String): DownloadEntity

    // 🔥 Observe everything (main screen)
    @Query("SELECT * FROM downloads WHERE is_deleted = 0 ORDER BY created_at DESC")
    fun observeAll(): Flow<List<DownloadEntity>>


    // 🔹 Filter by status (Completed / Failed / etc.)
    @Query("""
        SELECT * FROM downloads 
        WHERE status = :status AND is_deleted = 0
        ORDER BY created_at DESC
    """)
    fun observeByStatus(status: DownloadStatus): Flow<List<DownloadEntity>>


    // 🔹 Filter by playlist
    @Query("""
        SELECT * FROM downloads 
        WHERE playlist_id = :playlistId AND is_deleted = 0
        ORDER BY created_at DESC
    """)
    fun observeByPlaylist(playlistId: String): Flow<List<DownloadEntity>>


    // 🔥 Insert or replace
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(download: DownloadEntity)


    // 🔥 Bulk insert (playlist downloads)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(downloads: List<DownloadEntity>)


    // 🔹 Update progress (very important for download service)
    @Query("""
        UPDATE downloads 
        SET downloaded_bytes = :downloadedBytes,
            total_bytes = :totalBytes,
            status = :status
        WHERE task_id = :taskId
    """)
    suspend fun updateProgress(
        taskId: String,
        downloadedBytes: Long,
        totalBytes: Long?,
        status: DownloadStatus
    )


    // 🔹 Update status only
    @Query("""
        UPDATE downloads 
        SET status = :status,
            error_message = :error
        WHERE task_id = :taskId
    """)
    suspend fun updateStatus(
        taskId: String,
        status: DownloadStatus,
        error: String? = null
    )


    // 🔥 Soft delete single
    @Query("""
        UPDATE downloads 
        SET is_deleted = 1 
        WHERE task_id = :taskId
    """)
    suspend fun softDelete(taskId: String)


    // 🔥 Soft delete selected
    @Query("""
        UPDATE downloads 
        SET is_deleted = 1 
        WHERE task_id IN (:ids)
    """)
    suspend fun softDeleteSelected(ids: List<String>)


    // 🔥 Hard delete single
    @Query("DELETE FROM downloads WHERE task_id = :taskId")
    suspend fun hardDelete(taskId: String)


    // 🔥 Hard delete selected
    @Query("DELETE FROM downloads WHERE task_id IN (:ids)")
    suspend fun hardDeleteSelected(ids: List<String>)


    // 🔥 Delete everything
    @Query("DELETE FROM downloads")
    suspend fun deleteAll()


    // 🔥 Recovery after crash
    @Query("""
        UPDATE downloads
        SET status = 'FAILED'
        WHERE status = 'DOWNLOADING'
    """)
    suspend fun markIncompleteAsFailed()
}