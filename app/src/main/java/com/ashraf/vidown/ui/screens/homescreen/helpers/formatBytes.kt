package com.ashraf.vidown.ui.screens.homescreen.helpers

fun formatBytes(bytes: Long): String {
    val kb = 1024L
    val mb = kb * 1024
    val gb = mb * 1024

    return when {
        bytes >= gb -> String.format("%.2f GB", bytes.toFloat() / gb)
        bytes >= mb -> String.format("%.2f MB", bytes.toFloat() / mb)
        bytes >= kb -> String.format("%.2f KB", bytes.toFloat() / kb)
        else -> "$bytes B"
    }
}