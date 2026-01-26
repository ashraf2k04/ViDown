package com.ashraf.vidown.ui.screens.mainscreen.componets

import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Downloading
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar.NavItem
import com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar.IconSource
import com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar.CurvedBottomNavigation
import com.ashraf.vidown.ui.screens.mainscreen.componets.bottombar.CurveAnimationType
import com.ashraf.vidown.ui.navigation.Routes

@Composable
fun BottomNavBar(
    scaffoldRoute: String?,
    navController: NavController
) {
        val items = remember {
            listOf(
                NavItem(
                    icon = IconSource.Vector(Icons.Outlined.Home),
                    selectedIcon = IconSource.Vector(Icons.Filled.Home),
                    label = "Home",
                    route = Routes.HomeScreen.route
                ),
                NavItem(
                    icon = IconSource.Vector(Icons.Outlined.Downloading),
                    selectedIcon = IconSource.Vector(Icons.Filled.DownloadForOffline),
                    label = "Downloaded",
                    route = Routes.DownloadsScreen.route
                )
            )
        }

        val selectedIndex = items.indexOfFirst {
            it.route == scaffoldRoute
        }.coerceAtLeast(0)

        CurvedBottomNavigation(
            items = items,
            selectedIndex = selectedIndex,
            curveAnimationType = CurveAnimationType.SMOOTH,
            enableHapticFeedback = true,
            onItemSelected = { index ->
                navController.navigate(items[index].route) {
                    popUpTo(Routes.HomeScreen.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
//    }
}
