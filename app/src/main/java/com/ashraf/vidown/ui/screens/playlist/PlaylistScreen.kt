package com.ashraf.vidown.ui.screens.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ashraf.vidown.ui.navigation.Routes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PlaylistScreen(
    url: String,
    navController: NavController,
    viewModel: PlaylistViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(url)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                PlaylistEvent.NavigateToDownloads ->
                    navController.navigate(Routes.DownloadsScreen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
            }
        }
    }

    when {
        state.isLoading -> PlaylistLoadingUi()
        state.error != null -> ErrorUi(
            error = state.error!!,
            onRetry = { viewModel.init(url) }
        )
        else -> PlaylistContent(state, viewModel)
    }
}

@Composable
fun PlaylistLoadingUi() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ── Header shimmer ───────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmer()
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmer()
        )

        Spacer(Modifier.height(16.dp))

        // ── Action buttons shimmer ───────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmer()
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Playlist items shimmer ───────────────
        repeat(8) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer()
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmer()
                    )

                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmer()
                    )
                }
            }
        }
    }
}

fun Modifier.shimmer(): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmer")

    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            )
        ),
        label = "shimmer_translate"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    drawWithCache {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translate - 200f, 0f),
            end = Offset(translate, size.height)
        )
        onDrawBehind {
            drawRect(brush)
        }
    }
}


@Composable
fun PlaylistContent(
    state: PlaylistUiState,
    viewModel: PlaylistViewModel
) {
    Column {

        PlaylistHeader(
            title = state.title,
            totalCount = state.items.size,
            selectedCount = state.selectedCount,
            enableSelected = state.canDownloadSelected,
            onDownloadSelected = viewModel::onDownloadSelected,
            onDownloadAll = viewModel::onDownloadAll
        )

        LazyColumn {
            items(state.items, key = {  it.id ?: it.title!! }) { item ->
                PlaylistItemRow(
                    item = item,
                    onCheckedChange = { checked ->
                        viewModel.onItemChecked(item.id!!, checked)
                    }
                )
            }
        }
    }
}

@Composable
fun PlaylistItemRow(
    item: PlaylistItemUi,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = item.isSelected,
            onCheckedChange = onCheckedChange
        )

        Spacer(Modifier.width(8.dp))

        item.title?.let {
            Text(
                text = it,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



@Composable
fun PlaylistHeader(
    title: String,
    totalCount: Int,
    selectedCount: Int,
    onDownloadAll: () -> Unit,
    onDownloadSelected: () -> Unit,
    enableSelected: Boolean
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // ── Title row ─────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.ifBlank { "Playlist" },
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "$totalCount videos" +
                                if (selectedCount > 0) " • $selectedCount selected" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Action buttons ────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Download Selected
                Surface(
                    onClick = onDownloadSelected,
                    enabled = enableSelected,
                    shape = RoundedCornerShape(50),
                    color = if (enableSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Download selected",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        color = if (enableSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            Color.Gray,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Download All
                Surface(
                    onClick = onDownloadAll,
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondary
                ) {
                    Text(
                        text = "Download all",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorUi(
    error: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )

            onRetry?.let {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Retry",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { it() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

