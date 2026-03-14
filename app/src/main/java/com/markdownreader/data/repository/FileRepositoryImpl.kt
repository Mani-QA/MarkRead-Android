package com.markdownreader.data.repository

import android.net.Uri
import com.markdownreader.data.datasource.FileDataSource
import com.markdownreader.domain.model.FileLoadResult
import com.markdownreader.domain.model.MarkdownDocument
import com.markdownreader.domain.repository.FileRepository

class FileRepositoryImpl(private val fileDataSource: FileDataSource) : FileRepository {

    override suspend fun loadMarkdownFile(uri: Uri): FileLoadResult {
        return try {
            val fileSize = fileDataSource.getFileSize(uri)

            if (fileSize > FileDataSource.MAX_FILE_SIZE_BYTES) {
                return FileLoadResult.TooLarge
            }

            val content = fileDataSource.readFileContent(uri)

            if (content.isBlank()) {
                return FileLoadResult.Empty
            }

            val fileName = fileDataSource.getFileName(uri)
            FileLoadResult.Success(
                MarkdownDocument(
                    uri = uri,
                    fileName = fileName,
                    rawContent = content,
                    sizeBytes = fileSize
                )
            )
        } catch (e: SecurityException) {
            FileLoadResult.Error("Permission denied. Please grant file access and try again.")
        } catch (e: Exception) {
            FileLoadResult.Error(e.message ?: "Failed to load file.")
        }
    }
}
