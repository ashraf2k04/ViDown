package com.ashraf.vidown.domain

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.ashraf.vidown.App
import com.ashraf.vidown.domain.task.*
import com.ashraf.vidown.domain.DownloadState.Idle
import com.ashraf.vidown.domain.DownloadState.ReadyWithInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_CONCURRENCY = 3

@Singleton
class DownloadEngineImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : DownloadEngine {

    internal val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val taskStateMap = mutableStateMapOf<Task, Task.State>()
    private val snapshotFlow = snapshotFlow { taskStateMap.toMap() }

    init {
        observeRunningTasks()
        persistTasks()
    }

    override fun getTaskStateMap(): SnapshotStateMap<Task, Task.State> = taskStateMap


    override fun enqueue(task: Task) {
        taskStateMap[task] =
            Task.State(
                downloadState = Idle,
                videoInfo = null,
                viewState = ViewState(url = task.url, title = task.url),
            )
    }

    override fun enqueue(task: Task, state: Task.State) {
        taskStateMap[task] = state
    }

    override fun remove(task: Task): Boolean =
        taskStateMap.remove(task) != null

    override fun cancel(task: Task): Boolean =
        task.cancelImpl(taskStateMap)

    override fun restart(task: Task) =
        task.restartImpl(taskStateMap)

    private fun observeRunningTasks() = scope.launch {
        snapshotFlow
            .onEach { processQueue() }
            .map { it.countRunning() }
            .distinctUntilChanged()
            .collect { if (it > 0) App.startService() else App.stopService() }
    }

    private fun persistTasks() = scope.launch {
        snapshotFlow
            .map { it.filterNotCompleted() }
            .collect { it }
    }

    private fun processQueue() {
        if (taskStateMap.countRunning() >= MAX_CONCURRENCY) return

        taskStateMap.entries
            .sortedBy { it.value.downloadState }
            .firstOrNull { it.value.canStart }
            ?.let { (task, state) ->
                when (state.downloadState) {
                    Idle -> task.prepare(this)
                    ReadyWithInfo -> task.download(this, appContext)
                    else -> Unit
                }
            }
    }
}