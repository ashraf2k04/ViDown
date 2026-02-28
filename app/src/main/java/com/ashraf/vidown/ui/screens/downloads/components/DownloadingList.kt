package com.ashraf.vidown.ui.screens.downloads.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashraf.vidown.ui.screens.downloads.DownloadsViewModel
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus
import com.ashraf.vidown.ui.screens.homescreen.helpers.formatBytes

@Composable
fun DownloadingList(
    viewModel: DownloadsViewModel = hiltViewModel()
) {
    val items by viewModel.downloading.collectAsState(emptyList())

    LazyColumn {
        items(
            items = items,
            key = { it.taskId } // 🔑 important for progress updates
        ) { item ->

            Log.d(
                "DOWNLOAD_PROGRESS",
                "DOWNLOADING_LIST - stateUpdated progress=${item.progress / 100f}"
            )

            when (item.status) {
                DownloadStatus.DOWNLOADING ->
                    DownloadItem(
                        title = item.title,
                        progress = item.progress,
                        downloaded = formatBytes(item.downloadedBytes),
                        total = item.totalBytes?.let(::formatBytes) ?: "~",
                        onCancel = { viewModel.cancel(item.taskId) }
                    )

                else ->
                    FailedItem(
                        reason = item.error ?: "Canceled",
                        title = item.title
                    )
            }
        }
    }
}
