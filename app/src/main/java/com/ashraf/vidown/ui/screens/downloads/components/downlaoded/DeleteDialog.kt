package com.ashraf.vidown.ui.screens.downloads.components.downlaoded

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteDialog(
    onDeleteListOnly: () -> Unit,
    onDeleteFiles: () -> Unit,
    onCancel: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = {},
        title = {
            Text("Delete download")
        },
        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDeleteListOnly
                ) {
                    Text("Delete from list only")
                }

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDeleteFiles
                ) {
                    Text("Delete files also")
                }

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancel
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}