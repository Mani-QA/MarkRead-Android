package com.markdownreader.domain.repository

import android.net.Uri
import com.markdownreader.domain.model.FileLoadResult

interface FileRepository {
    suspend fun loadMarkdownFile(uri: Uri): FileLoadResult
}
