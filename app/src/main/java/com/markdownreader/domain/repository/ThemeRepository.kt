package com.markdownreader.domain.repository

import com.markdownreader.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val themeFlow: Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}
