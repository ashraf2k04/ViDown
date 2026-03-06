package com.ashraf.vidown.ui.screens.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashraf.vidown.domain.helpers.PlaylistEntry
import com.ashraf.vidown.domain.YtdlpDriverWrapper
import com.ashraf.vidown.domain.helpers.Thumbnail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val ytdlpDriverWrapper: YtdlpDriverWrapper
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistUiState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<PlaylistEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    /* ---------------- LOAD ---------------- */
    fun init(url: String) {
        if (_state.value.url == url) return

        _state.update {
            it.copy(
                url = url,
                isLoading = true,
                error = null
            )
        }

        fetchPlaylist(url)
    }

    private fun fetchPlaylist(url: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                ytdlpDriverWrapper.fetchPlaylistInfoDriver.fetchPlaylist(url, "playlist_preview")
            }

            Log.d("URL RESULT", result.toString())

            result
                .onSuccess { playlist ->
                    _state.update {
                        it.copy(
                            title = playlist.title.orEmpty(),
                            items = playlist.entries?.map { item->
                                PlaylistItemUi(
                                    id = item.id,
                                    title = item.title,
                                    url = item.url,
                                    thumbnails = item.thumbnails
                                )
                            } ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load playlist"
                        )
                    }
                }
        }
    }


    fun onItemChecked(id: String, checked: Boolean) {
        _state.update { state ->
            val updated = state.items.map {
                if (it.id == id) it.copy(isSelected = checked) else it
            }

            val selectedCount = updated.count { it.isSelected }

            state.copy(
                items = updated,
                selectedCount = selectedCount,
                canDownloadSelected = selectedCount > 0
            )
        }
    }

    /* ---------------- DOWNLOAD ACTIONS ---------------- */

    fun onDownloadAll() {
        enqueue(state.value.items)
    }

    fun onDownloadSelected() {
        enqueue(state.value.items.filter { it.isSelected })
    }

    private var taskId : String? = null

    private fun enqueue(items: List<PlaylistItemUi>) {

        if (items.isEmpty()) return

        val domainList: List<PlaylistEntry> =
            items.map { item: PlaylistItemUi -> item.toDomain() }

        taskId = "playlist_${System.currentTimeMillis()}"

        ytdlpDriverWrapper.downloadPlaylistDriver.downloadPlaylist(
            title = state.value.title,
            playlist = domainList,
            outputDir = "/storage/emulated/0/Download/Vidown/Playlists",
            formatSelector = "bestvideo+bestaudio/best",
            taskPrefix = taskId!!
        )

        _events.tryEmit(PlaylistEvent.NavigateToDownloads)
    }

    fun cancel(taskId: String) {
        ytdlpDriverWrapper.cancel(taskId)
    }
}

private fun PlaylistItemUi.toDomain(): PlaylistEntry =
    PlaylistEntry(
        id = id,
        title = title,
        url = url,
        thumbnails = thumbnails
    )

data class PlaylistUiState(
    val url: String? = null,

    val title: String = "",
    val items: List<PlaylistItemUi> = emptyList(),

    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedCount: Int = 0,
    val canDownloadSelected: Boolean = false
)

data class PlaylistItemUi(
    val id: String?,
    val title: String?,
    val url: String?,
    val duration: String? = null,
    val isSelected: Boolean = false,
    val thumbnails : List<Thumbnail>? = emptyList()
)

sealed interface PlaylistEvent {
    data object NavigateToDownloads : PlaylistEvent
}


