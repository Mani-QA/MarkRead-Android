package com.markdownreader.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.markdownreader.domain.model.RecentFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.recentFilesDataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_files")

class RecentFilesDataSource(private val context: Context) {

    companion object {
        private val RECENT_FILES_KEY = stringPreferencesKey("recent_files_json")
        private const val MAX_RECENT_FILES = 20
    }

    val recentFilesFlow: Flow<List<RecentFile>> = context.recentFilesDataStore.data.map { prefs ->
        deserialize(prefs[RECENT_FILES_KEY])
    }

    suspend fun addRecentFile(recentFile: RecentFile) {
        context.recentFilesDataStore.edit { prefs ->
            val existing = deserialize(prefs[RECENT_FILES_KEY]).toMutableList()
            existing.removeAll { it.uriString == recentFile.uriString }
            existing.add(0, recentFile)
            val trimmed = existing.take(MAX_RECENT_FILES)
            prefs[RECENT_FILES_KEY] = serialize(trimmed)
        }
    }

    suspend fun removeRecentFile(uriString: String) {
        context.recentFilesDataStore.edit { prefs ->
            val existing = deserialize(prefs[RECENT_FILES_KEY]).toMutableList()
            existing.removeAll { it.uriString == uriString }
            prefs[RECENT_FILES_KEY] = serialize(existing)
        }
    }

    suspend fun clearAll() {
        context.recentFilesDataStore.edit { prefs ->
            prefs.remove(RECENT_FILES_KEY)
        }
    }

    private fun serialize(files: List<RecentFile>): String {
        val array = JSONArray()
        files.forEach { file ->
            val obj = JSONObject().apply {
                put("uri", file.uriString)
                put("name", file.fileName)
                put("ts", file.timestampMs)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun deserialize(json: String?): List<RecentFile> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                RecentFile(
                    uriString = obj.getString("uri"),
                    fileName = obj.getString("name"),
                    timestampMs = obj.getLong("ts")
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
