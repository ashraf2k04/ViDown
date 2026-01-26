package com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val icon: IconSource,
    val selectedIcon: IconSource? = null,
    val label: String,
    val route: String,
    val badgeCount: Int? = null
)

sealed class IconSource {
    data class Vector(val imageVector: ImageVector) : IconSource()
    data class Drawable(@DrawableRes val resId: Int) : IconSource()
}