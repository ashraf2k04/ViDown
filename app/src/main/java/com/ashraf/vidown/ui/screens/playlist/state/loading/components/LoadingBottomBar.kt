package com.ashraf.vidown.ui.screens.playlist.state.loading.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ashraf.vidown.util.shimmer

@Composable
fun LoadingBottomBar() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
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
}