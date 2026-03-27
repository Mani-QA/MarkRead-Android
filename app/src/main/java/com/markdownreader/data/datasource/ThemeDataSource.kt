package com.markdownreader.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.markdownreader.domain.model.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
    }

    val themeFlow: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: AppTheme.LIGHT.name
        try {
            AppTheme.valueOf(themeName)
        } catch (_: IllegalArgumentException) {
            AppTheme.LIGHT
        }
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}
