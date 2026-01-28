package com.ashraf.vidown.domain.task

internal val TypeInfo.id: String
    get() = when (this) {
        is TypeInfo.Playlist -> "$index"
        TypeInfo.URL -> ""
    }

internal fun makeId(
    url: String,
    type: TypeInfo
): String = "${url}_${type.id}"