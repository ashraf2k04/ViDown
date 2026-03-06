package com.ashraf.vidown.ui.screens.downloads.components.downloading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus

@Composable
fun DownloadItem(
    title: String,
    progress: Float,            // 0f..1f
    downloaded: String,
    total: String,
    onCancel: () -> Unit,
    thumbnail: String?,
    status: DownloadStatus
) {

    val isPending = status == DownloadStatus.PENDING
    val isDownloading = status == DownloadStatus.DOWNLOADING

    val ringColor = when {
        isPending -> Color(0xFFFBC02D)     // Yellow
        isDownloading -> Color(0xFF2E7D32) // Green
        else -> MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Thumbnail + Progress Ring
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    model = thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(16.dp))
                )

                if (isPending) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(50))
                            .background(ringColor)
                            .align(Alignment.Center)
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(56.dp),
                        strokeWidth = 4.dp,
                        color = ringColor
                    )

                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Title + Progress Text
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = when {
                        isPending -> "• Pending"
                        else -> "${(progress * 100).toInt()}% · $downloaded / $total"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = when {
                        isPending -> Color(0xFFFBC02D)
                        isDownloading -> Color(0xFF2E7D32)
                        else -> Color.Gray
                    }
                )
            }

            Spacer(Modifier.width(8.dp))

            // Cancel Chip
            Surface(
                onClick = onCancel,
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = "Cancel",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}