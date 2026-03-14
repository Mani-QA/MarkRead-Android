package com.markdownreader.domain.usecase

import android.text.Spanned
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ParseMarkdownUseCase(private val markwon: Markwon) {

    suspend operator fun invoke(rawMarkdown: String): Spanned = withContext(Dispatchers.Default) {
        markwon.toMarkdown(rawMarkdown)
    }
}
