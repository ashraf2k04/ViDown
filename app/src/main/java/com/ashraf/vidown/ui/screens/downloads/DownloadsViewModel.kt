package com.ashraf.vidown.ui.screens.downloads

import android.util.Log
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStateBus
import com.ashraf.vidown.ui.screens.downloads.helpers.DownloadStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashraf.vidown.domain.YtdlpDriverWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val ytldpDriverWrapper: YtdlpDriverWrapper
) : ViewModel() {

    val downloading =
        DownloadStateBus.downloads.map {
            it.filter { d ->
                d.status == DownloadStatus.DOWNLOADING ||
                        d.status == DownloadStatus.PENDING ||
                        d.status == DownloadStatus.FAILED ||
                        d.status == DownloadStatus.CANCELED
            }

        }

    init {
        viewModelScope.launch {
            downloading.collect { list ->
                list.forEach { item ->
                    Log.d(
                        "DOWNLOAD_PROGRESS",
                        "DOWNLOAD_VIEW_MODEL - ViewModel progress=${item.progress} bytes=${item.downloadedBytes}"
                    )
                }
            }
        }
    }


    val downloaded =
        DownloadStateBus.downloads.map {
            it.filter { d -> d.status == DownloadStatus.COMPLETED }
        }

    fun cancel(taskId: String) {
       ytldpDriverWrapper.downloadVideoDriver.cancel(taskId)
    }


}
