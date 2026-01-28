package com.ashraf.vidown.ui.screens.homescreen

import androidx.lifecycle.ViewModel
import com.ashraf.vidown.domain.download.Downloader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class HomeViewModel @Inject constructor(
    // later you can inject:
    private val downloader: Downloader,
    // private val repository: HomeRepository,
    // private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onUrlChange(url: String) {
        _uiState.update { it.copy(url = url) }
    }

    fun onSearchOrDownloadClick() {
        _uiState.update { it.copy(showQualitySheet = true) }
    }

    fun onQualitySelected(quality: String) {
        _uiState.update { it.copy(selectedQuality = quality) }
    }

    fun dismissQualitySheet() {
        _uiState.update {
            it.copy(
                showQualitySheet = false,
                selectedQuality = null
            )
        }
    }

    fun onDownloadConfirmed() {
        val state = _uiState.value
        val url = state.url
        val quality = state.selectedQuality ?: return
        if (url.isBlank()) return

        Log.d("FLOW", "Download confirmed: url=$url")

        downloader.fetchAndDownload(
            url = url,
            outputDir = "/storage/emulated/0/Download",
            taskId = url.hashCode().toString()
        )

        dismissQualitySheet()
    }
}