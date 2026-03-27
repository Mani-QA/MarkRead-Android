package com.markdownreader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.markdownreader.data.local.entity.ReadPositionEntity

@Dao
interface ReadPositionDao {

    @Query("SELECT * FROM read_positions WHERE uriString = :uriString")
    suspend fun getPosition(uriString: String): ReadPositionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ReadPositionEntity)

    @Query("DELETE FROM read_positions WHERE uriString = :uriString")
    suspend fun deleteByUri(uriString: String)
}
