package com.ashraf.vidown.ui.screens.downloads.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
@Composable
fun DownloadedList() {
    Column {
        DownloadedItem(
            title = "Easy Vegan Recipes",
            size = "250MB"
        )
    }
}