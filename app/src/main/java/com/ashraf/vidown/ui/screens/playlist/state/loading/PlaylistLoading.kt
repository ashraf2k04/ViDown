package com.ashraf.vidown.ui.screens.playlist.state.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.playlist.state.loading.components.LoadingBottomBar
import com.ashraf.vidown.ui.screens.playlist.state.loading.components.LoadingHeader
import com.ashraf.vidown.ui.screens.playlist.state.loading.components.LoadingList

@Composable
fun PlaylistLoading(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LoadingHeader()

        Spacer(Modifier.height(24.dp))

        LoadingList()

        Spacer(Modifier.height(16.dp))

        LoadingBottomBar()
    }
}
