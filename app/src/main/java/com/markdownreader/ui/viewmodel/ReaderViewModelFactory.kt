package com.markdownreader.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markdownreader.di.AppContainer

class ReaderViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReaderViewModel::class.java)) {
            return ReaderViewModel(
                loadMarkdownFileUseCase = container.loadMarkdownFileUseCase,
                parseMarkdownUseCase = container.parseMarkdownUseCase,
                themeRepository = container.themeRepository,
                recentFilesRepository = container.recentFilesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
