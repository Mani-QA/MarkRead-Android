package com.markdownreader.domain.model

data class ReadPosition(
    val uriString: String,
    val scrollOffset: Int,
    val lastReadTimestampMs: Long
)
