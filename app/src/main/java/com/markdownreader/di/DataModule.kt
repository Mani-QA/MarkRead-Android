package com.markdownreader.di

import android.content.Context
import com.markdownreader.data.datasource.FileDataSource
import com.markdownreader.data.datasource.ThemeDataSource
import com.markdownreader.data.local.dao.ReadPositionDao
import com.markdownreader.data.local.dao.RecentFileDao
import com.markdownreader.data.repository.FileRepositoryImpl
import com.markdownreader.data.repository.ReadPositionRepositoryImpl
import com.markdownreader.data.repository.RecentFilesRepositoryImpl
import com.markdownreader.data.repository.ThemeRepositoryImpl
import com.markdownreader.domain.repository.FileRepository
import com.markdownreader.domain.repository.ReadPositionRepository
import com.markdownreader.domain.repository.RecentFilesRepository
import com.markdownreader.domain.repository.ThemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFileDataSource(@ApplicationContext context: Context): FileDataSource {
        return FileDataSource(context)
    }

    @Provides
    @Singleton
    fun provideThemeDataSource(@ApplicationContext context: Context): ThemeDataSource {
        return ThemeDataSource(context)
    }

    @Provides
    @Singleton
    fun provideFileRepository(fileDataSource: FileDataSource): FileRepository {
        return FileRepositoryImpl(fileDataSource)
    }

    @Provides
    @Singleton
    fun provideThemeRepository(themeDataSource: ThemeDataSource): ThemeRepository {
        return ThemeRepositoryImpl(themeDataSource)
    }

    @Provides
    @Singleton
    fun provideRecentFilesRepository(recentFileDao: RecentFileDao): RecentFilesRepository {
        return RecentFilesRepositoryImpl(recentFileDao)
    }

    @Provides
    @Singleton
    fun provideReadPositionRepository(readPositionDao: ReadPositionDao): ReadPositionRepository {
        return ReadPositionRepositoryImpl(readPositionDao)
    }
}
