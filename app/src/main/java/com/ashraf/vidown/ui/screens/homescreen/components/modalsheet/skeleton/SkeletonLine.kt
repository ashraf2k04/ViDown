package com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.playlist.shimmer

@Composable
fun SkeletonLine(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth(0.6f)
            .height(20.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .shimmer()
    )
}