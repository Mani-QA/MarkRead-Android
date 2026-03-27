package com.markdownreader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.markdownreader.data.local.dao.ReadPositionDao
import com.markdownreader.data.local.dao.RecentFileDao
import com.markdownreader.data.local.entity.ReadPositionEntity
import com.markdownreader.data.local.entity.RecentFileEntity

@Database(
    entities = [RecentFileEntity::class, ReadPositionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentFileDao(): RecentFileDao
    abstract fun readPositionDao(): ReadPositionDao
}
