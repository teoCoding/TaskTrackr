package com.teocoding.tasktrackr.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getDarkMode(): Flow<Int>

    suspend fun setDarkMode(darkMode: Int)
}