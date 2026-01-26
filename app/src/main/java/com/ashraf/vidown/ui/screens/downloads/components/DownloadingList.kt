package com.ashraf.vidown.ui.screens.downloads.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
@Composable
fun DownloadingList() {
    Column {
        DownloadItem(
            title = "Funny Cat Compilation",
            progress = 0.75f
        )

        FailedItem("Network Error")
    }
}