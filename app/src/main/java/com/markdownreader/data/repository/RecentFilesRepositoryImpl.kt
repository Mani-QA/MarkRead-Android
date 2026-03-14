package com.markdownreader.data.repository

import com.markdownreader.data.datasource.RecentFilesDataSource
import com.markdownreader.domain.model.RecentFile
import com.markdownreader.domain.repository.RecentFilesRepository
import kotlinx.coroutines.flow.Flow

class RecentFilesRepositoryImpl(
    private val dataSource: RecentFilesDataSource
) : RecentFilesRepository {

    override val recentFilesFlow: Flow<List<RecentFile>> = dataSource.recentFilesFlow

    override suspend fun addRecentFile(recentFile: RecentFile) {
        dataSource.addRecentFile(recentFile)
    }

    override suspend fun removeRecentFile(uriString: String) {
        dataSource.removeRecentFile(uriString)
    }

    override suspend fun clearAll() {
        dataSource.clearAll()
    }
}
