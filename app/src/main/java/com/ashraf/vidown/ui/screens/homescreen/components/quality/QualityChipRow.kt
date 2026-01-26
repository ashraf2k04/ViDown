package com.ashraf.vidown.ui.screens.homescreen.components.quality

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QualityChipRow(
    selectedQuality: String?,
    onQualitySelected: (String) -> Unit
) {
    val qualities = listOf("1080p", "720p", "480p")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        qualities.forEach { quality ->
            QualityChip(
                label = quality,
                selected = selectedQuality == quality,
                onClick = { onQualitySelected(quality) }
            )
        }
    }
}