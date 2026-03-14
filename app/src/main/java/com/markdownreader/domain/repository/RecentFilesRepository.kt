package com.markdownreader.domain.repository

import com.markdownreader.domain.model.RecentFile
import kotlinx.coroutines.flow.Flow

interface RecentFilesRepository {
    val recentFilesFlow: Flow<List<RecentFile>>
    suspend fun addRecentFile(recentFile: RecentFile)
    suspend fun removeRecentFile(uriString: String)
    suspend fun clearAll()
}
