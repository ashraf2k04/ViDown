package com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurvedBottomNavigation(
    items: List<NavItem>,
    selectedIndex: Int,
    bottomBarAlignment: Alignment = Alignment.BottomCenter,
    curveAnimationType: CurveAnimationType = CurveAnimationType.SMOOTH,
    animationDuration: Int = 300,
    fabSize: Dp = 45.dp,
    fabIconSize: Dp = 32.dp,
    enableFabIconScale: Boolean = false,
    navBarHeight: Dp = 56.dp,
    unselectedIconSize: Dp = 24.dp,
    unselectedTextSize: TextUnit = 11.sp,
    fabElevation: Dp = 12.dp,
    curveRadius: Dp = 47.dp,
    curveDepth: Dp = 22.dp,
    curveSmoothness: Dp = 25.dp,
    enableHapticFeedback: Boolean = false,
    onItemSelected: (Int) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = bottomBarAlignment
    ) {
        val componentWidth = maxWidth

        CurvedBottomNavigationContent(
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = onItemSelected,
            componentWidth = componentWidth,
            fabSize = fabSize,
            fabIconSize = fabIconSize,
            navBarHeight = navBarHeight,
            unselectedIconSize = unselectedIconSize,
            unselectedTextSize = unselectedTextSize,
            fabElevation = fabElevation,
            curveRadius = curveRadius,
            curveDepth = curveDepth,
            curveSmoothness = curveSmoothness,
            curveAnimationType = curveAnimationType,
            animationDuration = animationDuration,
            enableHapticFeedback = enableHapticFeedback,
            enableFabIconScale = enableFabIconScale
        )
    }
}
