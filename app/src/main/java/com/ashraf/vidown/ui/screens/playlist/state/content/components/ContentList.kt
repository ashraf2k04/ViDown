package com.ashraf.vidown.ui.screens.playlist.state.content.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.ui.screens.playlist.PlaylistViewModel
import com.ashraf.vidown.ui.screens.playlist.model.PlaylistUiState

@Composable
fun ContentList(
    modifier : Modifier,
    state: PlaylistUiState,
    viewModel: PlaylistViewModel
){
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = state.items,
            key = { it.id ?: it.title!! }
        ) { item ->

            ContentItem(
                item = item,
                onClick = {
                    viewModel.onItemChecked(
                        item.id!!,
                        !item.isSelected
                    )
                }
            )
        }
    }
}