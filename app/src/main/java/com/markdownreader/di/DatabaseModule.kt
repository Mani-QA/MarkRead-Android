package com.markdownreader.di

import android.content.Context
import androidx.room.Room
import com.markdownreader.data.local.AppDatabase
import com.markdownreader.data.local.dao.ReadPositionDao
import com.markdownreader.data.local.dao.RecentFileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "markread.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideRecentFileDao(db: AppDatabase): RecentFileDao = db.recentFileDao()

    @Provides
    fun provideReadPositionDao(db: AppDatabase): ReadPositionDao = db.readPositionDao()
}
