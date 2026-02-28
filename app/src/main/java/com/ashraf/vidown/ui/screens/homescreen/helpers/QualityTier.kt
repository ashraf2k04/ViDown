package com.ashraf.vidown.ui.screens.homescreen.helpers

sealed class QualityTier(
    val label: String,
    val maxHeight: Int?
) {
    object Best : QualityTier("Best", null)
    object High : QualityTier("High (≤4K)", 2160)
    object Medium : QualityTier("Medium (≤1080p)", 1080)
    object Low : QualityTier("Low (≤480p)", 480)
}