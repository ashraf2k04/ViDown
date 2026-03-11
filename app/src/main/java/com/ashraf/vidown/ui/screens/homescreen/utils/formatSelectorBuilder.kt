package com.ashraf.vidown.ui.screens.homescreen.utils

import com.ashraf.vidown.ui.screens.homescreen.model.HomeUiState
import com.ashraf.vidown.ui.screens.homescreen.model.QualityTier

fun formatSelectorBuilder(state: HomeUiState): String {
    val tier = state.selectedQualityTier ?: QualityTier.Best
    val format = state.selectedFormat ?: "mp4"

    return when {
        // 🎧 AUDIO ONLY
        state.audioOnly -> {
            when (format) {
                "mp3" -> "bestaudio[ext=mp3]/bestaudio"
                "m4a" -> "bestaudio[ext=m4a]/bestaudio"
                else -> "bestaudio"
            }
        }

        // 🎥 VIDEO TIERS
        tier.maxHeight != null -> {
            "bestvideo[height<=${tier.maxHeight}][ext=$format]+bestaudio/best"
        }

        // 🏆 BEST
        else -> "best"
    }
}