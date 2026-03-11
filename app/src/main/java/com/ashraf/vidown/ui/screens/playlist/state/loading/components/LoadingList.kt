package com.ashraf.vidown.ui.screens.playlist.state.loading.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun LoadingList() {

    Column {

        repeat(8) {
            LoadingItem()
        }

    }
}