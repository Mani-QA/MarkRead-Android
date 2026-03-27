package com.markdownreader.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markdownreader.domain.model.AppTheme
import com.markdownreader.domain.model.FileLoadResult
import com.markdownreader.domain.model.ReadPosition
import com.markdownreader.domain.model.RecentFile
import com.markdownreader.domain.repository.ReadPositionRepository
import com.markdownreader.domain.repository.RecentFilesRepository
import com.markdownreader.domain.repository.ThemeRepository
import com.markdownreader.domain.usecase.LoadMarkdownFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ReaderUiState {
    data object Idle : ReaderUiState
    data object Loading : ReaderUiState
    data class Success(
        val fileName: String,
        val rawMarkdown: String,
        val uriString: String,
        val initialScrollOffset: Int = 0
    ) : ReaderUiState
    data class Error(val message: String) : ReaderUiState
    data object EmptyFile : ReaderUiState
    data object FileTooLarge : ReaderUiState
}

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val loadMarkdownFileUseCase: LoadMarkdownFileUseCase,
    private val themeRepository: ThemeRepository,
    private val recentFilesRepository: RecentFilesRepository,
    private val readPositionRepository: ReadPositionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReaderUiState>(ReaderUiState.Idle)
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    val currentTheme: StateFlow<AppTheme> = themeRepository.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.LIGHT)

    val recentFiles: StateFlow<List<RecentFile>> = recentFilesRepository.observeRecentFiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadFile(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = ReaderUiState.Loading

            when (val result = loadMarkdownFileUseCase(uri)) {
                is FileLoadResult.Success -> {
                    val position = readPositionRepository.getPosition(uri.toString())
                    _uiState.value = ReaderUiState.Success(
                        fileName = result.document.fileName,
                        rawMarkdown = result.document.rawContent,
                        uriString = uri.toString(),
                        initialScrollOffset = position?.scrollOffset ?: 0
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
                    _uiState.value = ReaderUiState.Error(result.message)
                }
                FileLoadResult.Empty -> {
                    _uiState.value = ReaderUiState.EmptyFile
                }
                FileLoadResult.TooLarge -> {
                    _uiState.value = ReaderUiState.FileTooLarge
                }
            }
        }
    }

    fun saveScrollPosition(uriString: String, scrollOffset: Int) {
        viewModelScope.launch {
            readPositionRepository.savePosition(
                ReadPosition(
                    uriString = uriString,
                    scrollOffset = scrollOffset,
                    lastReadTimestampMs = System.currentTimeMillis()
                )
            )
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
        _uiState.value = ReaderUiState.Idle
    }
}
