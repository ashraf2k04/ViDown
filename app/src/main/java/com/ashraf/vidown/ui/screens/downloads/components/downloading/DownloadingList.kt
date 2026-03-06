package com.ashraf.vidown.ui.screens.downloads.components.downloading

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashraf.vidown.ui.screens.downloads.DownloadVM
import com.ashraf.vidown.ui.screens.downloads.components.PlaylistGroupItem
import com.ashraf.vidown.ui.screens.downloads.helpers.*
import com.ashraf.vidown.ui.screens.homescreen.helpers.formatBytes

@Composable
fun DownloadingList(
    viewModel: DownloadVM = hiltViewModel()
) {
    val items by viewModel.downloading.collectAsState(emptyList())

    LazyColumn {

        items(items) { item ->

            when (item) {

                is DownloadListItem.Single -> {

                    val data = item.item

                    when (data.status) {

                        DownloadStatus.PENDING ->
                            DownloadItem(
                                title = data.title,
                                progress = data.progress,
                                downloaded = formatBytes(data.downloadedBytes),
                                total = data.totalBytes?.let(::formatBytes) ?: "~",
                                onCancel = { viewModel.cancel(data.taskId) },
                                thumbnail = data.imageUrl,
                                status = data.status
                            )

                        DownloadStatus.DOWNLOADING ->
                            DownloadItem(
                                title = data.title,
                                progress = data.progress,
                                downloaded = formatBytes(data.downloadedBytes),
                                total = data.totalBytes?.let(::formatBytes) ?: "~",
                                onCancel = { viewModel.cancel(data.taskId) },
                                thumbnail = data.imageUrl,
                                status = data.status
                            )

                        else ->
                            FailedItem(
                                status = data.status,
                                reason = data.error!!,
                                title = data.title,
                                onRetry = null,
                                thumbnail = data.imageUrl!!,

                                )
                    }
                }

                is DownloadListItem.PlaylistGroup -> {

                    val progress =
                        viewModel.calculatePlaylistProgress(item.items)

                    PlaylistGroupItem(
                        title = "${item.playlistTitle} " +
                                "(${item.items.count { it.status == DownloadStatus.COMPLETED }}/${item.items.size})",
                        progress = progress,
                        isExpanded = item.isExpanded,
                        onToggle = {
                            viewModel.togglePlaylist(item.playlistId)
                        }
                    ) {


                        item.items.forEach { data ->
                            when (data.status) {

                                DownloadStatus.PENDING ->
                                    DownloadItem(
                                        title = data.title,
                                        progress = data.progress,
                                        downloaded = formatBytes(data.downloadedBytes),
                                        total = data.totalBytes?.let(::formatBytes) ?: "~",
                                        onCancel = { viewModel.cancel(data.taskId) },
                                        thumbnail = data.imageUrl,
                                        status = data.status
                                    )

                                DownloadStatus.DOWNLOADING -> {

                                    Log.d("TASK", data.taskId)
                                    Log.d("TASK", data.toString())

                                    DownloadItem(
                                        title = data.title,
                                        progress = data.progress,
                                        downloaded = formatBytes(data.downloadedBytes),
                                        total = data.totalBytes?.let(::formatBytes) ?: "~",
                                        onCancel = { viewModel.cancel(data.taskId) },
                                        thumbnail = data.imageUrl,
                                        status = data.status
                                    )
                                }

                                else -> {
                                    Log.d("TASK", data.taskId)
                                    Log.d("TASK", data.toString())
                                    FailedItem(
                                        status = data.status,
                                        reason = data.error ?: "Cancelled",
                                        title = data.title,
                                        onRetry = null,
                                        thumbnail = data.imageUrl!!
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}