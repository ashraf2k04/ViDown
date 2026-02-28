package com.ashraf.vidown.ui.screens.homescreen.components.modalsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.homescreen.helpers.HomeUiState
import com.ashraf.vidown.ui.screens.homescreen.helpers.QualityTier
import com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.quality.QualityTierChipRow
import com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.skeleton.SkeletonButton
import com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.skeleton.SkeletonChipRow
import com.ashraf.vidown.ui.screens.homescreen.components.modalsheet.skeleton.SkeletonLine
import com.ashraf.vidown.ui.screens.homescreen.helpers.formatBytes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPropertyBottomSheet(
    show: Boolean,
    state: HomeUiState,
    onQualitySelected: (QualityTier) -> Unit,
    onFormatSelected: (String) -> Unit,
    onAudioToggle: (Boolean) -> Unit,
    onDownload: () -> Unit,
    onDismiss: () -> Unit
){

    if (!show) return

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState
    ) {
        LaunchedEffect(state.isFetchingInfo) {
            if (!state.isFetchingInfo) {
                sheetState.expand()
            }
        }

        when {

            state.isFetchingInfo -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    SkeletonLine()
                    Spacer(Modifier.height(16.dp))
                    SkeletonChipRow()
                    Spacer(Modifier.height(24.dp))
                    SkeletonButton()
                }
            }

            state.fetchError != null -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Failed to fetch video info",
                        style = MaterialTheme.typography.titleMedium
                    )


                    Spacer(Modifier.height(8.dp))


                    Text(
                        text = state.fetchError,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    state.title?.let { title  ->

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(16.dp))


                        AnimatedVisibility(
                            visible = !state.audioOnly,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(Modifier.fillMaxWidth()) {
                                QualityTierChipRow(
                                    selectedTier = state.selectedQualityTier,
                                    onTierSelected = onQualitySelected
                                )

                                FormatChipRow(
                                    selectedFormat = state.selectedFormat,
                                    onFormatSelected = onFormatSelected
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        AudioOnlyToggle(
                            checked = state.audioOnly,
                            onCheckedChange = onAudioToggle
                        )

                        Spacer(Modifier.height(16.dp))

                        AnimatedVisibility(
                            visible = state.estimatedFileSize != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = "Estimated size: ${formatBytes(state.estimatedFileSize ?: 0L)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    val canDownload =
                        state.hasVideoInfo &&
                                state.selectedQualityTier != null &&
                                state.selectedFormat != null

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        // Cancel button (LEFT)
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss
                        ) {
                            Text("Cancel")
                        }

                        // Download button (RIGHT)
                        Button(
                            modifier = Modifier.weight(1f),
                            enabled = canDownload,
                            onClick = onDownload
                        ) {
                            Text("Download")
                        }
                    }

                }
            }
        }
    }
}

