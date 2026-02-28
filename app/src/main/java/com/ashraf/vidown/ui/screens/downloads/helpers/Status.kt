package com.ashraf.vidown.ui.screens.downloads.helpers

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    CANCELED;

    fun canTransitionTo(new: DownloadStatus): Boolean {
        return when (this) {

            PENDING ->
                new == DOWNLOADING ||
                        new == CANCELED

            DOWNLOADING ->
                new == COMPLETED ||
                        new == FAILED ||
                        new == CANCELED

            COMPLETED ->
                false

            FAILED ->
                false

            CANCELED ->
                false
        }
    }
}