package com.ashraf.vidown.domain.helpers

fun VideoInfo.estimateSize(): Long? {
    // 1️⃣ Best case: requested formats (actual selected)
    requestedFormats
        ?.mapNotNull { it.fileSize ?: it.fileSizeApprox }
        ?.sum()
        ?.let { return it.toLong() }

    // 2️⃣ Fallback: format itself
    fileSize?.let { return it.toLong() }
    fileSizeApprox?.let { return it.toLong() }

    // 3️⃣ Worst case: unknown
    return null
}
