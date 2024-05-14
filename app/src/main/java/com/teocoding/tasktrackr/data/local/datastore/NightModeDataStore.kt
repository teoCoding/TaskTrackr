package com.teocoding.tasktrackr.data.local.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.teocoding.tasktrackr.data.local.datastore.DarkModeDataStore.Companion.DARK_MODE_PREFERENCES
import com.teocoding.tasktrackr.data.utils.getInt
import com.teocoding.tasktrackr.data.utils.setInt
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.nightModeDataStore: DataStore<Preferences> by preferencesDataStore(name = DARK_MODE_PREFERENCES)

@Singleton
class DarkModeDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val DARK_MODE_PREFERENCES = "night_mode_preferences"

        val DARK_MODE = intPreferencesKey("dark_mode")
    }

    private val nightModeDataStore = context.nightModeDataStore


    val darkMode = nightModeDataStore.getInt(
        key = DARK_MODE,
        defaultValue = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )


    suspend fun setDarkMode(value: Int) =
        nightModeDataStore.setInt(DARK_MODE, value)

}
