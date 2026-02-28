package com.ashraf.vidown.ui.screens.downloads.helpers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object DownloadStateBus {

    private val _downloads =
        MutableStateFlow<List<DownloadUiItem>>(emptyList())

    val downloads: StateFlow<List<DownloadUiItem>> = _downloads

    fun upsert(item: DownloadUiItem) {
        _downloads.value =
            _downloads.value
                .filterNot { it.taskId == item.taskId } + item
    }

    fun markCompleted(taskId: String) {
        update(taskId) {
            it.copy(status = DownloadStatus.COMPLETED, progress = 1f)
        }
    }

    fun markFailed(taskId: String, reason: String) {
        update(taskId) {
            it.copy(status = DownloadStatus.FAILED, error = reason)
        }
    }

    fun markCanceled(taskId: String) {
        update(taskId) {
            it.copy(status = DownloadStatus.CANCELED, error = "Canceled")
        }
    }

    private fun update(taskId: String, transform: (DownloadUiItem) -> DownloadUiItem) {
        _downloads.value =
            _downloads.value.map {
                if (it.taskId == taskId) transform(it) else it
            }
    }

    fun markPending(item: DownloadUiItem) {
        upsert(item.copy(status = DownloadStatus.PENDING, progress = 0f))
    }

}
