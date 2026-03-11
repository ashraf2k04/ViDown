package com.ashraf.vidown.ui.screens.homescreen.components.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormatChipRow(
    selectedFormat: String?,
    formats: List<String> = listOf("mp4", "webm", "mp3"),
    onFormatSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "File format",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            formats.forEach { format ->
                AnimatedFilterChip(
                    selected = selectedFormat == format,
                    onClick = { onFormatSelected(format) },
                    label = { Text(format.uppercase()) }
                )
            }
        }
    }
}