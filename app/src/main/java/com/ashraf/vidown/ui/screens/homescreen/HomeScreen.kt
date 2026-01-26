package com.ashraf.vidown.ui.screens.homescreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ashraf.vidown.ui.screens.homescreen.components.DownloadButton
import com.ashraf.vidown.ui.screens.homescreen.components.SearchBar
import com.ashraf.vidown.ui.screens.homescreen.components.SelectPropertyBottomSheet

@Composable
fun HomeScreen(
    navController: NavController
) {
    var url by remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(
//                        Color(0xFFFCE0D0),
//                        Color(0xFFE6E8FF)
//                    )
//                )
//            )
    ) {

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            SearchBar(
                value = url,
                onValueChange = { url = it },
                onSearch = { showSheet = true }
            )

            Spacer(Modifier.height(32.dp))

            DownloadButton { showSheet = true }
        }

        if (showSheet) {
            SelectPropertyBottomSheet(
                show = showSheet,
                onDismiss = { showSheet = false }
            )
        }
    }
}