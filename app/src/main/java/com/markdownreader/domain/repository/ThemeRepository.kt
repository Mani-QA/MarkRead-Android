package com.markdownreader.domain.repository

import com.markdownreader.domain.model.AppTheme
import com.markdownreader.domain.model.ReadingPreferences
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val themeFlow: Flow<AppTheme>
    val readingPreferencesFlow: Flow<ReadingPreferences>
    suspend fun setTheme(theme: AppTheme)
    suspend fun setReadingPreferences(prefs: ReadingPreferences)
}
