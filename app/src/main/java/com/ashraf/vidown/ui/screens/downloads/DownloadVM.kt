package com.ashraf.vidown.ui.screens.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashraf.vidown.database.DownloadEntity
import com.ashraf.vidown.database.DownloadRepository
import com.ashraf.vidown.domain.YtdlpDriverWrapper
import com.ashraf.vidown.ui.screens.downloads.helpers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class DownloadVM @Inject constructor(
    private val ytldpDriverWrapper: YtdlpDriverWrapper,
    private val repository: DownloadRepository,
) : ViewModel() {

    private val _expandedPlaylists =
        MutableStateFlow<Set<String>>(emptySet())

    /* ---------------- GROUPING ---------------- */

    private fun groupDownloads(
        items: List<DownloadUiItem>,
        expanded: Set<String>
    ): List<DownloadListItem> {

        val grouped = items.groupBy { it.playlistId }

        return grouped.flatMap { (playlistId, list) ->
            if (playlistId == null) {
                list.map { DownloadListItem.Single(it) }
            } else {
                listOf(
                    DownloadListItem.PlaylistGroup(
                        playlistId = playlistId,
                        playlistTitle = list.first().playlistTitle ?: "Playlist",
                        items = list,
                        isExpanded = expanded.contains(playlistId)
                    )
                )
            }
        }
    }

    /* ---------------- SOURCE (DB instead of StateBus) ---------------- */

    private val downloadsFlow =
        repository.observeAll()
            .map { entities ->
                entities.map { it.toUiItem() }
            }

    /* ---------------- DOWNLOADING ---------------- */

    val downloading =
        combine(
            downloadsFlow,
            _expandedPlaylists
        ) { list, expanded ->

            val filtered = list.filter {
                it.status == DownloadStatus.DOWNLOADING ||
                        it.status == DownloadStatus.PENDING ||
                        it.status == DownloadStatus.FAILED ||
                        it.status == DownloadStatus.CANCELED
            }

            groupDownloads(filtered, expanded)
        }

    /* ---------------- DOWNLOADED ---------------- */

    val downloaded =
        combine(
            downloadsFlow,
            _expandedPlaylists
        ) { list, expanded ->

            val filtered = list.filter {
                it.status == DownloadStatus.COMPLETED
            }

            groupDownloads(filtered, expanded)
        }

    /* ---------------- ACTIONS ---------------- */

    fun togglePlaylist(id: String) {
        val current = _expandedPlaylists.value
        _expandedPlaylists.value =
            if (current.contains(id))
                current - id
            else
                current + id
    }

    fun cancel(taskId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ytldpDriverWrapper.cancel(taskId)

                repository.updateStatus(
                    taskId = taskId,
                    status = DownloadStatus.CANCELED,
                    error = "Canceled"
                )
            }
        }
    }

    /* ---------------- UTIL ---------------- */

    fun calculatePlaylistProgress(
        items: List<DownloadUiItem>
    ): Float {

        val downloaded = items.sumOf { it.downloadedBytes }
        val total = items.sumOf { it.totalBytes ?: 0L }

        if (total == 0L) return 0f

        return downloaded.toFloat() / total
    }
}

fun DownloadEntity.toUiItem(): DownloadUiItem {

    val progress = if (totalBytes == null || totalBytes == 0L) {
        0f
    } else {
        downloadedBytes.toFloat() / totalBytes.toFloat()
    }

    return DownloadUiItem(
        taskId = taskId,
        title = title,
        progress = progress,
        downloadedBytes = downloadedBytes,
        totalBytes = totalBytes,
        status = status,
        error = error,
        playlistId = playlistId,
        playlistTitle = playlistTitle,
        imageUrl = imageUrl,
        filepath = filePath,
        createdAt = createdAt
    )
}