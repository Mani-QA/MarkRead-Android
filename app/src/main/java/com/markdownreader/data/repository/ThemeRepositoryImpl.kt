package com.markdownreader.data.repository

import com.markdownreader.data.datasource.ThemeDataSource
import com.markdownreader.domain.model.AppTheme
import com.markdownreader.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(private val themeDataSource: ThemeDataSource) : ThemeRepository {

    override val themeFlow: Flow<AppTheme> = themeDataSource.themeFlow

    override suspend fun setTheme(theme: AppTheme) {
        themeDataSource.saveTheme(theme)
    }
}
