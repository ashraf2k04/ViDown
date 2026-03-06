package com.ashraf.vidown.ui.screens.downloads.components.downlaoded

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashraf.vidown.ui.screens.downloads.DownloadVM
import com.ashraf.vidown.ui.screens.downloads.components.PlaylistGroupItem
import com.ashraf.vidown.ui.screens.downloads.helpers.*
import com.ashraf.vidown.ui.screens.homescreen.helpers.formatBytes

@Composable
fun DownloadedList(
    viewModel: DownloadVM = hiltViewModel()
) {
    val items by viewModel.downloaded.collectAsState(emptyList())

    LazyColumn {

        items(items) { item ->

            when (item) {

                is DownloadListItem.Single -> {

                    val data = item.item

                    DownloadedItem(
                        thumbnail = data.imageUrl!!,
                        title = data.title,
                        size = formatBytes(data.downloadedBytes)
                    )
                }

                is DownloadListItem.PlaylistGroup -> {

                    PlaylistGroupItem(
                        title = "${item.playlistTitle} " +
                                "(${item.items.size}/${item.items.size})",
                        progress = 1f,
                        isExpanded = item.isExpanded,
                        onToggle = {
                            viewModel.togglePlaylist(item.playlistId)
                        }
                    ) {

                        item.items.forEach { video ->

                            DownloadedItem(
                                thumbnail = video.imageUrl!!,
                                title = video.title,
                                size = formatBytes(video.downloadedBytes)
                            )
                        }
                    }
                }
            }
        }
    }
}