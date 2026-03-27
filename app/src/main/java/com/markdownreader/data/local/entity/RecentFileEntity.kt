package com.markdownreader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_files")
data class RecentFileEntity(
    @PrimaryKey
    val uriString: String,
    val fileName: String,
    val timestampMs: Long
)
