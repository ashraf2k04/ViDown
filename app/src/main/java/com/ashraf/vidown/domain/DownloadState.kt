package com.ashraf.vidown.domain

import com.ashraf.vidown.domain.RestartableAction
import kotlinx.coroutines.Job
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed interface DownloadState : Comparable<DownloadState> {

    interface Cancelable {
        @Transient val job: Job
        val taskId: String
        val action: RestartableAction
    }

    interface Restartable {
        val action: RestartableAction
    }

    @Serializable
    data object Idle : DownloadState

    @Serializable
    data class FetchingInfo(
        @Transient override val job: Job = Job(),
        override val taskId: String,
    ) : DownloadState, Cancelable {
        override val action = RestartableAction.FetchInfo
    }

    @Serializable
    data object ReadyWithInfo : DownloadState

    @Serializable
    data class Running(
        @Transient override val job: Job = Job(),
        override val taskId: String,
        val progress: Float = -1f,
        val progressText: String = "",
    ) : DownloadState, Cancelable {
        override val action = RestartableAction.Download
    }

    @Serializable
    data class Canceled(
        override val action: RestartableAction,
        val progress: Float? = null,
    ) : DownloadState, Restartable

    @Serializable
    data class Error(
        @Transient val throwable: Throwable = Throwable(),
        override val action: RestartableAction,
    ) : DownloadState, Restartable

    @Serializable
    data class Completed(val filePath: String?) : DownloadState

    override fun compareTo(other: DownloadState): Int =
        ordinal - other.ordinal

    private val ordinal: Int
        get() = when (this) {
            is Running -> 0
            ReadyWithInfo -> 1
            is FetchingInfo -> 2
            Idle -> 3
            is Canceled -> 4
            is Error -> 5
            is Completed -> 6
        }
}