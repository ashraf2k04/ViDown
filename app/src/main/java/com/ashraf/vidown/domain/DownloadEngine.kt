package com.ashraf.vidown.domain

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.ashraf.vidown.domain.task.Task

interface DownloadEngine {
    fun getTaskStateMap(): SnapshotStateMap<Task, Task.State>
    fun cancel(task: Task): Boolean
    fun restart(task: Task)
    fun enqueue(task: Task)
    fun enqueue(task: Task, state: Task.State)
    fun remove(task: Task): Boolean

    fun cancel(taskId: String): Boolean =
        getTaskStateMap().keys.find { it.id == taskId }?.let { cancel(it) } ?: false
}