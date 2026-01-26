package com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun CurvedBottomNavigationContent(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    componentWidth: Dp,
    fabSize: Dp,
    fabIconSize: Dp,
    navBarHeight: Dp,
    unselectedIconSize: Dp,
    unselectedTextSize: TextUnit,
    fabElevation: Dp,
    curveRadius: Dp,
    curveDepth: Dp,
    curveSmoothness: Dp,
    curveAnimationType: CurveAnimationType,
    animationDuration: Int,
    enableHapticFeedback: Boolean,
    enableFabIconScale: Boolean,
) {
    val haptic = LocalHapticFeedback.current

    var currentIndex by remember { mutableStateOf(selectedIndex) }
    var previousIndex by remember { mutableStateOf(selectedIndex) }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex != currentIndex) {
            previousIndex = currentIndex
            currentIndex = selectedIndex
        }
    }

    val cellWidth = componentWidth / items.size
    val startX = cellWidth * previousIndex + (cellWidth / 2) - (fabSize / 2)
    val endX = cellWidth * currentIndex + (cellWidth / 2) - (fabSize / 2)

    val fabX = remember { Animatable(startX.value) }
    val fabY = remember { Animatable(-35f) }
    val iconScale = remember { Animatable(1f) }

    var displayedIconIndex by remember { mutableStateOf(currentIndex) }

    LaunchedEffect(currentIndex) {
        if (currentIndex != previousIndex) {
            val start = startX.value
            val end = endX.value
            val distance = end - start
            var iconSwitched = false

            launch {
                fabX.animateTo(
                    targetValue = end,
                    animationSpec = tween(animationDuration, easing = FastOutSlowInEasing)
                ) {
                    val progress =
                        if (distance != 0f) (value - start) / distance else 0f

                    if (!iconSwitched && progress >= 0.3f) {
                        displayedIconIndex = currentIndex
                        iconSwitched = true
                    }

                    val curveHeight = 40f
                    val parabolicY = -4f * progress * (progress - 1f)
                    launch {
                        fabY.snapTo(-30f + curveHeight * parabolicY)
                    }
                }
            }

            if (!iconSwitched) {
                displayedIconIndex = currentIndex
            }
        }
    }

    LaunchedEffect(currentIndex) {
        iconScale.animateTo(1.25f, tween(140))
        iconScale.animateTo(
            1f,
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {

        FloatingActionButton(
            onClick = {},
            elevation = FloatingActionButtonDefaults.elevation(fabElevation),
            shape = RoundedCornerShape(fabSize),
            modifier = Modifier
                .offset(x = fabX.value.dp, y = fabY.value.dp)
                .size(fabSize)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                RenderIcon(
                    iconSource = items[displayedIconIndex].selectedIcon
                        ?: items[displayedIconIndex].icon,
                    modifier = Modifier.size(
                        if (enableFabIconScale)
                            (fabIconSize.value * iconScale.value).dp
                        else fabIconSize
                    )
                )
            }
        }

        CurvedNavigationBar(
            selectedIndex = currentIndex,
            itemCount = items.size,
            animationType = curveAnimationType,
            curveRadius = curveRadius,
            curveDepth = curveDepth,
            curveSmoothness = curveSmoothness,
            navBarHeight = navBarHeight,
            animationDuration = animationDuration
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                BottomNavItem(
                    item = item,
                    isSelected = index == currentIndex,
                    onClick = {
                        if (enableHapticFeedback) {
                            haptic.performHapticFeedback(
                                HapticFeedbackType.Confirm
                            )
                        }
                        previousIndex = currentIndex
                        currentIndex = index
                        onItemSelected(index)
                    },
                    modifier = Modifier.weight(1f),
                    iconSize = unselectedIconSize,
                    textSize = unselectedTextSize,
                )
            }
        }
    }
}
