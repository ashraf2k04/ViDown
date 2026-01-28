package com.ashraf.vidown.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface RestartableAction {
    @Serializable
    data object FetchInfo : RestartableAction
    @Serializable
    data object Download : RestartableAction
}