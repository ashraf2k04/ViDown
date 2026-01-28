package com.ashraf.vidown.domain.task

import kotlinx.serialization.Serializable

@Serializable
sealed interface TypeInfo {

    @Serializable
    data class Playlist(val index: Int = 0) : TypeInfo

    @Serializable
    data object URL : TypeInfo
}