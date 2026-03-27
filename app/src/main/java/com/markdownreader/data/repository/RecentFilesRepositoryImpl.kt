package com.markdownreader.data.repository

import com.markdownreader.data.local.dao.RecentFileDao
import com.markdownreader.data.local.entity.RecentFileEntity
import com.markdownreader.domain.model.RecentFile
import com.markdownreader.domain.repository.RecentFilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentFilesRepositoryImpl @Inject constructor(
    private val recentFileDao: RecentFileDao
) : RecentFilesRepository {

    override fun observeRecentFiles(): Flow<List<RecentFile>> {
        return recentFileDao.observeRecentFiles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addRecentFile(recentFile: RecentFile) {
        recentFileDao.upsert(recentFile.toEntity())
    }

    override suspend fun removeRecentFile(uriString: String) {
        recentFileDao.deleteByUri(uriString)
    }

    override suspend fun clearAll() {
        recentFileDao.deleteAll()
    }

    private fun RecentFileEntity.toDomain() = RecentFile(
        uriString = uriString,
        fileName = fileName,
        timestampMs = timestampMs
    )

    private fun RecentFile.toEntity() = RecentFileEntity(
        uriString = uriString,
        fileName = fileName,
        timestampMs = timestampMs
    )
}
