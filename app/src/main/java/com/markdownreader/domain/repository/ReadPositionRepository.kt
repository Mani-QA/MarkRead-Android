package com.markdownreader.domain.repository

import com.markdownreader.domain.model.ReadPosition

interface ReadPositionRepository {
    suspend fun getPosition(uriString: String): ReadPosition?
    suspend fun savePosition(position: ReadPosition)
}
