package com.markdownreader.ui.viewmodel

import android.net.Uri
import android.text.Spanned
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markdownreader.domain.model.AppTheme
import com.markdownreader.domain.model.FileLoadResult
import com.markdownreader.domain.model.RecentFile
import com.markdownreader.domain.repository.RecentFilesRepository
import com.markdownreader.domain.repository.ThemeRepository
import com.markdownreader.domain.usecase.LoadMarkdownFileUseCase
import com.markdownreader.domain.usecase.ParseMarkdownUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ReaderUiState(
    val isLoading: Boolean = false,
    val fileName: String = "",
    val renderedContent: Spanned? = null,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
    val isTooLarge: Boolean = false,
    val hasDocument: Boolean = false
)

class ReaderViewModel(
    private val loadMarkdownFileUseCase: LoadMarkdownFileUseCase,
    private val parseMarkdownUseCase: ParseMarkdownUseCase,
    private val themeRepository: ThemeRepository,
    private val recentFilesRepository: RecentFilesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    val currentTheme: StateFlow<AppTheme> = themeRepository.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.LIGHT)

    val recentFiles: StateFlow<List<RecentFile>> = recentFilesRepository.recentFilesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadFile(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = ReaderUiState(isLoading = true)

            when (val result = loadMarkdownFileUseCase(uri)) {
                is FileLoadResult.Success -> {
                    val spanned = parseMarkdownUseCase(result.document.rawContent)
                    _uiState.value = ReaderUiState(
                        fileName = result.document.fileName,
                        renderedContent = spanned,
                        hasDocument = true
                    )
                    recentFilesRepository.addRecentFile(
                        RecentFile(
                            uriString = uri.toString(),
                            fileName = result.document.fileName,
                            timestampMs = System.currentTimeMillis()
                        )
                    )
                }
                is FileLoadResult.Error -> {
                    _uiState.value = ReaderUiState(errorMessage = result.message)
                }
                FileLoadResult.Empty -> {
                    _uiState.value = ReaderUiState(isEmpty = true)
                }
                FileLoadResult.TooLarge -> {
                    _uiState.value = ReaderUiState(isTooLarge = true)
                }
            }
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.setTheme(theme)
        }
    }

    fun removeRecentFile(uriString: String) {
        viewModelScope.launch {
            recentFilesRepository.removeRecentFile(uriString)
        }
    }

    fun clearRecentFiles() {
        viewModelScope.launch {
            recentFilesRepository.clearAll()
        }
    }

    fun clearDocument() {
        _uiState.value = ReaderUiState()
    }
}
