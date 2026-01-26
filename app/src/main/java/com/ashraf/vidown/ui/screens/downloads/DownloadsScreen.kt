package com.ashraf.vidown.ui.screens.downloads

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.downloads.components.DownloadedList
import com.ashraf.vidown.ui.screens.downloads.components.DownloadingList
import com.ashraf.vidown.ui.screens.downloads.components.DownloadsTabs

@Composable
fun DownloadsScreen() {
    var selectedTab by remember { mutableStateOf(DownloadTab.DOWNLOADING) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
    ) {

        DownloadsTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(Modifier.height(16.dp))

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                // Decide direction based on tab change
                val direction = when {
                    initialState == DownloadTab.DOWNLOADING &&
                            targetState == DownloadTab.DOWNLOADED -> 1   // → right
                    initialState == DownloadTab.DOWNLOADED &&
                            targetState == DownloadTab.DOWNLOADING -> -1 // ← left
                    else -> 0
                }

                slideInHorizontally(
                    initialOffsetX = { it * direction }
                ) + fadeIn() togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { -it * direction }
                        ) + fadeOut()
            },
            label = "downloads-tabs"
        ) { tab ->
            when (tab) {
                DownloadTab.DOWNLOADING -> DownloadingList()
                DownloadTab.DOWNLOADED -> DownloadedList()
            }
        }
    }
}