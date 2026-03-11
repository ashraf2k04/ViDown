package com.ashraf.vidown.ui.screens.homescreen.model

data class HomeUiState(
    val url: String = "",

    val showQualitySheet: Boolean = false,
    val isFetchingInfo: Boolean = false,
    val fetchError: String? = null,

    // UI-only data
    val title: String? = null,
    val thumbnail: String? = null,
    val hasVideoInfo: Boolean = false,

    val selectedQualityTier: QualityTier? = null,
    val selectedFormat: String? = null,
    val audioOnly: Boolean = false,

    val estimatedFileSize: Long? = null
)