package com.ashraf.vidown.ui.screens.downloads.components.downlaoded

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashraf.vidown.ui.screens.downloads.DownloadVM
import com.ashraf.vidown.ui.screens.downloads.components.EmptyState
import com.ashraf.vidown.ui.screens.downloads.components.common.PlaylistGroupItem
import com.ashraf.vidown.ui.screens.downloads.utils.playVideo
import com.ashraf.vidown.ui.screens.downloads.model.*
import com.ashraf.vidown.ui.screens.homescreen.utils.byteFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadedList(
    viewModel: DownloadVM = hiltViewModel()
) {
    val items by viewModel.downloaded.collectAsState(emptyList())

    val selectionMode by viewModel.selectionMode.collectAsState()
    val selectedIds by viewModel.selectedIds.collectAsState()

    var deleteTarget by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (items.isEmpty()) {

        EmptyState(
            "Nothing Downloaded",
            "Downloaded videos will appear here."
        )

    } else {
        Column {

            if (selectionMode) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            showDeleteDialog = true
                        }
                    ) {
                        Text("Delete (${selectedIds.size})")
                    }

                    TextButton(
                        onClick = {
                            viewModel.clearSelection()
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }

            LazyColumn {

                items(
                    items = items,
                    key = {
                        when (it) {
                            is DownloadListItem.Single -> it.item.taskId
                            is DownloadListItem.PlaylistGroup -> it.playlistId
                        }
                    }
                ) { item ->

                    when (item) {

                        is DownloadListItem.Single -> {

                            val data = item.item

                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->

                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        deleteTarget = data.taskId
                                        showDeleteDialog = true
                                    }

                                    false
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                enableDismissFromEndToStart = !selectionMode,
                                backgroundContent = {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color.Red),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                    }
                                }
                            ) {
                                DownloadedItem(
                                    thumbnail = data.imageUrl!!,
                                    title = data.title,
                                    size = byteFormatter(data.downloadedBytes),
                                    selected = selectedIds.contains(data.taskId),
                                    selectionMode = selectionMode,
                                    onClick = {
                                        if (selectionMode) {
                                            viewModel.toggleSelection(data.taskId)
                                        } else {
                                            playVideo(context, data.filepath)
                                        }
                                    },
                                    onLongPress = {
                                        viewModel.enterSelection(data.taskId)
                                    }
                                )
                            }
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

                                    val dismissState = rememberSwipeToDismissBoxState(
                                        confirmValueChange = { value ->

                                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                                deleteTarget = video.taskId
                                                showDeleteDialog = true
                                            }

                                            false
                                        }
                                    )

                                    SwipeToDismissBox(
                                        state = dismissState,
                                        enableDismissFromStartToEnd = false,
                                        enableDismissFromEndToStart = !selectionMode,
                                        backgroundContent = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(20.dp))
                                                    .background(Color.Red),
                                                contentAlignment = Alignment.CenterEnd
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    ) {
                                        DownloadedItem(
                                            thumbnail = video.imageUrl!!,
                                            title = video.title,
                                            size = byteFormatter(video.downloadedBytes),
                                            selected = selectedIds.contains(video.taskId),
                                            selectionMode = selectionMode,
                                            onClick = {
                                                if (selectionMode) {
                                                    viewModel.toggleSelection(video.taskId)
                                                } else {
                                                    playVideo(context, video.filepath)
                                                }
                                            },
                                            onLongPress = {
                                                viewModel.enterSelection(video.taskId)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (showDeleteDialog) {

                DeleteDialog(

                    onDeleteListOnly = {

                        if (deleteTarget != null)
                            viewModel.deleteSingle(deleteTarget!!, false)
                        else
                            viewModel.deleteSelected(false)

                        showDeleteDialog = false
                        deleteTarget = null
                    },

                    onDeleteFiles = {

                        if (deleteTarget != null)
                            viewModel.deleteSingle(deleteTarget!!, true)
                        else
                            viewModel.deleteSelected(true)

                        showDeleteDialog = false
                        deleteTarget = null
                    },

                    onCancel = {
                        showDeleteDialog = false
                        deleteTarget = null
                    }
                )
            }
        }
    }
}