package com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun RenderIcon(
    iconSource: IconSource,
    contentDescription: String? = null,
    modifier: Modifier
) {
    when (iconSource) {
        is IconSource.Vector -> {
            Icon(
                imageVector = iconSource.imageVector,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }

        is IconSource.Drawable -> {
            Icon(
                painter = painterResource(id = iconSource.resId),
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
    }
}
