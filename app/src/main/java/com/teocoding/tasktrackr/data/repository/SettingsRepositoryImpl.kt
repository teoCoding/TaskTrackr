package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.data.local.datastore.DarkModeDataStore
import com.teocoding.tasktrackr.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val darkModeDataStore: DarkModeDataStore
) : SettingsRepository {

    override fun getDarkMode(): Flow<Int> {
        return darkModeDataStore.darkMode
    }

    override suspend fun setDarkMode(darkMode: Int) {
        darkModeDataStore.setDarkMode(darkMode)
    }
}