package com.markdownreader.data.datasource

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class FileDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024
    }

    suspend fun readFileContent(uri: Uri): String = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        } ?: throw IllegalStateException("Unable to open input stream for URI: $uri")
    }

    fun getFileSize(uri: Uri): Long {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            if (sizeIndex >= 0) cursor.getLong(sizeIndex) else -1L
        } ?: -1L
    }

    fun getFileName(uri: Uri): String {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            if (nameIndex >= 0) cursor.getString(nameIndex) else "Unknown"
        } ?: uri.lastPathSegment ?: "Unknown"
    }
}
