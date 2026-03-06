package com.ashraf.vidown.ui.screens.homescreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.ashraf.vidown.domain.helpers.VideoInfo
import com.ashraf.vidown.domain.YtdlpDriverWrapper
import com.ashraf.vidown.ui.screens.homescreen.helpers.HomeUiState
import com.ashraf.vidown.ui.screens.homescreen.helpers.QualityTier
import com.ashraf.vidown.ui.screens.homescreen.helpers.buildFormatSelector
import com.ashraf.vidown.ui.screens.playlist.isPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ytldpDriverWrapper: YtdlpDriverWrapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    // 🔒 DOMAIN CACHE (NOT exposed to UI)
    private var cachedVideoInfo: VideoInfo? = null

    /* ---------------- UI EVENTS ---------------- */

    fun onUrlChange(url: String) {
        _uiState.update { it.copy(url = url) }
    }

    private val _events = MutableSharedFlow<HomeEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )

    val events = _events.asSharedFlow()


    fun onSearchOrDownloadClick() {
        val url = _uiState.value.url
        if (url.isBlank()) return

        if (isPlaylist(url)) {
            _events.tryEmit(HomeEvent.OpenPlaylist(url))
            return
        }

        // reset UI-only state
        _uiState.update {
            it.copy(
                showQualitySheet = true,
                isFetchingInfo = true,
                fetchError = null,
                title = null,
                thumbnail = null,
                hasVideoInfo = false,
                selectedQualityTier = null,
                selectedFormat = null,
                audioOnly = false,
                estimatedFileSize = null
            )
        }
        _uiState.update { it.copy(url = "") }

        fetchInfo(url)
    }

    /* ---------------- FETCH ---------------- */

    private fun fetchInfo(url: String){
        viewModelScope.launch(Dispatchers.IO) {

            val result = ytldpDriverWrapper.fetchVideoInfoDriver.fetchVideoInfo(
                url = url,
                taskKey = url.hashCode().toString()
            )

            result
                .onSuccess { info ->
                    cachedVideoInfo = info

                    _uiState.update {
                        it.copy(
                            title = info.title,
                            thumbnail = info.thumbnail,
                            hasVideoInfo = true,
                            isFetchingInfo = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isFetchingInfo = false,
                            fetchError = error.message
                        )
                    }
                }
        }
    }

    /* ---------------- USER SELECTIONS ---------------- */

    fun onQualitySelected(tier: QualityTier) {
        _uiState.update { state ->
            state.copy(
                selectedQualityTier = tier,
                estimatedFileSize = estimateSize(tier, state.audioOnly)
            )
        }
    }

    fun onFormatSelected(format: String) {
        _uiState.update { state ->
            state.copy(
                selectedFormat = format,
                estimatedFileSize = estimateSize(
                    state.selectedQualityTier,
                    state.audioOnly
                )
            )
        }
    }

    fun onAudioToggle(audioOnly: Boolean) {
        _uiState.update { state ->
            val correctedFormat =
                if (audioOnly && state.selectedFormat == "mp4") "m4a"
                else state.selectedFormat

            state.copy(
                audioOnly = audioOnly,
                selectedFormat = correctedFormat,
                estimatedFileSize = estimateSize(
                    state.selectedQualityTier,
                    audioOnly
                )
            )
        }
    }

    /* ---------------- DOWNLOAD ---------------- */

    fun onDownloadConfirmed() {
        val info = cachedVideoInfo ?: return
        val state = _uiState.value

        val formatSelector = buildFormatSelector(state)

        ytldpDriverWrapper.downloadVideoDriver.download(
            info = info,
            formatSelector = formatSelector,
            outputDir = "/storage/emulated/0/Download/Vidown",
            taskId = info.id ?: info.originalUrl.hashCode().toString()
        )

        dismissQualitySheet()
    }

    fun dismissQualitySheet() {
        _uiState.update {
            it.copy(
                showQualitySheet = false,
                selectedQualityTier = null
            )
        }
    }

    /* ---------------- HELPERS ---------------- */

    private fun estimateSize(
        tier: QualityTier?,
        audioOnly: Boolean
    ): Long? {
        if (tier == null) return null

        if (audioOnly) return 5L * 1024 * 1024

        return when (tier) {
            QualityTier.Best -> 200L * 1024 * 1024
            QualityTier.High -> 150L * 1024 * 1024
            QualityTier.Medium -> 80L * 1024 * 1024
            QualityTier.Low -> 40L * 1024 * 1024
        }
    }
}

sealed interface HomeEvent {
    data class OpenPlaylist(val url: String) : HomeEvent
}
