package com.ashraf.vidown.ui.screens.homescreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.homescreen.components.quality.QualityChipRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPropertyBottomSheet(
    show: Boolean,
    selectedQuality: String?,
    onQualitySelected: (String) -> Unit,
    onDownload: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Text(
                "Select Video Quality",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            QualityChipRow(
                selectedQuality = selectedQuality,
                onQualitySelected = onQualitySelected
            )

            Spacer(Modifier.height(24.dp))

            Button(
                enabled = selectedQuality != null,
                onClick = onDownload,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download")
            }
        }
    }
}