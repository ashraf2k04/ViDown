package com.ashraf.vidown.ui.screens.downloads.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashraf.vidown.ui.screens.downloads.DownloadsViewModel
import com.ashraf.vidown.ui.screens.downloads.helpers.formatBytes

@Composable
fun DownloadedList(
    viewModel: DownloadsViewModel = hiltViewModel()
) {
    val items by viewModel.downloaded.collectAsState(emptyList())

    LazyColumn {
        items(
            items = items,
            key = { it.taskId } // 🔑 stable key
        ) { item ->
            DownloadedItem(
                title = item.title,
                size = formatBytes(item.downloadedBytes)
            )
        }
    }
}
