package com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*

@Composable
fun BottomNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    textSize: TextUnit = 12.sp
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .bounceClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = !isSelected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RenderIcon(
                    iconSource = item.icon,
                    contentDescription = item.label,
                    modifier = Modifier.size(iconSize)
                )
                Text(text = item.label, fontSize = textSize)
            }
        }
    }
}
