package com.markdownreader.domain.model

data class RecentFile(
    val uriString: String,
    val fileName: String,
    val timestampMs: Long
)
