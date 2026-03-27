package com.markdownreader.domain.model

sealed interface FileLoadResult {
    data class Success(val document: MarkdownDocument) : FileLoadResult
    data class Error(val message: String) : FileLoadResult
    data object Empty : FileLoadResult
    data object TooLarge : FileLoadResult
}
