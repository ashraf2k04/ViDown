package com.ashraf.vidown.ui.screens.mainscreen


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ashraf.vidown.ui.navigation.NavigationHost
import com.ashraf.vidown.ui.screens.mainscreen.componets.BottomNavBar
import com.ashraf.vidown.ui.screens.mainscreen.componets.TopBar


@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(WindowInsets.navigationBars.asPaddingValues()),

        topBar = {
            TopBar(
                title = "Vidown"
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                scaffoldRoute = null
            )
        }
    ) { innerPadding ->
            NavigationHost(
                modifier = Modifier.padding(innerPadding).systemBarsPadding(),
                navController = navController
            )
    }
}

