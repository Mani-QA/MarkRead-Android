package com.markdownreader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_positions")
data class ReadPositionEntity(
    @PrimaryKey
    val uriString: String,
    val scrollOffset: Int,
    val lastReadTimestampMs: Long
)
