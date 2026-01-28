package com.ashraf.vidown.domain.task

import com.ashraf.vidown.domain.DownloadState.*

internal fun Map<Task, Task.State>.countRunning(): Int =
    count {
        it.value.downloadState is Running ||
                it.value.downloadState is FetchingInfo
    }

internal fun Map<Task, Task.State>.filterNotCompleted() =
    filterValues { it.downloadState !is Completed }

internal val Task.State.canStart: Boolean
    get() = downloadState == Idle || downloadState == ReadyWithInfo