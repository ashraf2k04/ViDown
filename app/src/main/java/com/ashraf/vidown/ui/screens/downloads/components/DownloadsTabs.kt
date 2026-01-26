package com.ashraf.vidown.ui.screens.downloads.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.downloads.DownloadTab

@Composable
fun DownloadsTabs(
    selectedTab: DownloadTab,
    onTabSelected: (DownloadTab) -> Unit
) {
    val tabs = DownloadTab.entries.toTypedArray()
    val shape = RoundedCornerShape(90)

    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        indicator = {},
        divider = {},
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(shape)
            .border(
                shape = shape,
                width = 2.dp,
                color = Color.DarkGray
            )
    ) {
        tabs.forEach { tab ->
            val selected = tab == selectedTab

            Tab(
                selected = selected,
                onClick = { onTabSelected(tab) },
                modifier = Modifier
                    .padding(4.dp)
                    .clip(shape)
                    .then(
                        if (selected) {
                            Modifier.border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                shape = shape
                            )
                                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                        } else Modifier
                    )
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            ) {
                Text(
                    text = if (tab == DownloadTab.DOWNLOADING)
                        "Downloading"
                    else
                        "Downloaded",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}