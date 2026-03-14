package com.markdownreader.domain.model

import android.net.Uri

data class MarkdownDocument(
    val uri: Uri,
    val fileName: String,
    val rawContent: String,
    val sizeBytes: Long
)
