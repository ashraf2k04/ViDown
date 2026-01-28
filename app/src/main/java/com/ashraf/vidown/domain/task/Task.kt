package com.ashraf.vidown.domain.task

import com.ashraf.vidown.domain.download.VideoInfo
import com.ashraf.vidown.domain.DownloadState
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val url: String,
    val type: TypeInfo = TypeInfo.URL,
    val id: String = makeId(url, type),
) : Comparable<Task> {

    val timeCreated: Long = System.currentTimeMillis()

    override fun compareTo(other: Task): Int =
        timeCreated.compareTo(other.timeCreated)

    @Serializable
    data class State(
        val downloadState: DownloadState,
        val videoInfo: VideoInfo?,
        val viewState: ViewState,
    )
}