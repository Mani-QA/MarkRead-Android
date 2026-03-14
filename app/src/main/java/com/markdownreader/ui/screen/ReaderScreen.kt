package com.markdownreader.ui.screen

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markdownreader.ui.components.AboutDialog
import com.markdownreader.ui.components.EmptyFileState
import com.markdownreader.ui.components.ErrorState
import com.markdownreader.ui.components.FileTooLargeState
import com.markdownreader.ui.components.MarkdownContent
import com.markdownreader.ui.components.RecentFilesList
import com.markdownreader.ui.components.ThemeMenu
import com.markdownreader.ui.viewmodel.ReaderViewModel
import io.noties.markwon.Markwon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    markwon: Markwon,
    versionName: String,
    onOpenFile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()
    val recentFiles by viewModel.recentFiles.collectAsState()
    var showAbout by remember { mutableStateOf(false) }

    if (showAbout) {
        AboutDialog(
            versionName = versionName,
            onDismiss = { showAbout = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.hasDocument) uiState.fileName else "MarkRead",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    if (uiState.hasDocument) {
                        IconButton(onClick = { viewModel.clearDocument() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                        }
                    }
                },
                actions = {
                    if (!uiState.hasDocument) {
                        IconButton(onClick = { showAbout = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "About"
                            )
                        }
                    }
                    IconButton(onClick = onOpenFile) {
                        Icon(
                            imageVector = Icons.Outlined.FolderOpen,
                            contentDescription = "Open file"
                        )
                    }
                    ThemeMenu(
                        currentTheme = currentTheme,
                        onThemeSelected = { viewModel.setTheme(it) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                uiState.errorMessage != null -> {
                    ErrorState(message = uiState.errorMessage!!)
                }
                uiState.isEmpty -> {
                    EmptyFileState()
                }
                uiState.isTooLarge -> {
                    FileTooLargeState()
                }
                uiState.hasDocument && uiState.renderedContent != null -> {
                    MarkdownContent(
                        spanned = uiState.renderedContent!!,
                        markwon = markwon
                    )
                }
                else -> {
                    RecentFilesList(
                        recentFiles = recentFiles,
                        onFileClick = { uriString ->
                            viewModel.loadFile(Uri.parse(uriString))
                        },
                        onRemoveFile = { uriString ->
                            viewModel.removeRecentFile(uriString)
                        },
                        onClearAll = { viewModel.clearRecentFiles() },
                        onOpenFile = onOpenFile
                    )
                }
            }
        }
    }
}
