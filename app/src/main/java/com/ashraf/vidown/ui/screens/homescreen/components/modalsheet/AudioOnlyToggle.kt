package com.ashraf.vidown.ui.screens.homescreen.components.modalsheet

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
fun AudioOnlyToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column {
        Text(
            text = "Audio",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            AnimatedFilterChip(
                selected = !checked,
                onClick = { onCheckedChange(false) },
                label = { Text("Video + Audio") }
            )

            AnimatedFilterChip(
                selected = checked,
                onClick = { onCheckedChange(true) },
                label = { Text("Audio only") }
            )
        }
    }
}
