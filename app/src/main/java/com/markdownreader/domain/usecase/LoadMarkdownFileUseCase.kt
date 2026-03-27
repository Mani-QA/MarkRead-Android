package com.markdownreader.domain.usecase

import android.net.Uri
import com.markdownreader.domain.model.FileLoadResult
import com.markdownreader.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadMarkdownFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(uri: Uri): FileLoadResult = withContext(Dispatchers.IO) {
        fileRepository.loadMarkdownFile(uri)
    }
}
