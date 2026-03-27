package com.markdownreader.data.repository

import com.markdownreader.data.local.dao.ReadPositionDao
import com.markdownreader.data.local.entity.ReadPositionEntity
import com.markdownreader.domain.model.ReadPosition
import com.markdownreader.domain.repository.ReadPositionRepository
import javax.inject.Inject

class ReadPositionRepositoryImpl @Inject constructor(
    private val readPositionDao: ReadPositionDao
) : ReadPositionRepository {

    override suspend fun getPosition(uriString: String): ReadPosition? {
        return readPositionDao.getPosition(uriString)?.toDomain()
    }

    override suspend fun savePosition(position: ReadPosition) {
        readPositionDao.upsert(position.toEntity())
    }

    private fun ReadPositionEntity.toDomain() = ReadPosition(
        uriString = uriString,
        scrollOffset = scrollOffset,
        lastReadTimestampMs = lastReadTimestampMs
    )

    private fun ReadPosition.toEntity() = ReadPositionEntity(
        uriString = uriString,
        scrollOffset = scrollOffset,
        lastReadTimestampMs = lastReadTimestampMs
    )
}
