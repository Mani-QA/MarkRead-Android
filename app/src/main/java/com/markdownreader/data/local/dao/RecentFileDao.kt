package com.markdownreader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.markdownreader.data.local.entity.RecentFileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentFileDao {

    @Query("SELECT * FROM recent_files ORDER BY timestampMs DESC LIMIT 20")
    fun observeRecentFiles(): Flow<List<RecentFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: RecentFileEntity)

    @Query("DELETE FROM recent_files WHERE uriString = :uriString")
    suspend fun deleteByUri(uriString: String)

    @Query("DELETE FROM recent_files")
    suspend fun deleteAll()
}
