package com.teocoding.tasktrackr.data.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

fun DataStore<Preferences>.getInt(key: Preferences.Key<Int>, defaultValue: Int = 0): Flow<Int> {
    return data.catch { exception ->
        if (exception is IOException) { // 2
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun DataStore<Preferences>.setInt(key: Preferences.Key<Int>, value: Int) =
    edit { dataStore ->
        dataStore[key] = value
    }

fun DataStore<Preferences>.getString(
    key: Preferences.Key<String>,
    defaultValue: String? = null
): Flow<String?> {
    return data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun DataStore<Preferences>.setString(key: Preferences.Key<String>, value: String) =
    edit { dataStore ->
        dataStore[key] = value
    }

fun DataStore<Preferences>.getBoolean(
    key: Preferences.Key<Boolean>,
    defaultValue: Boolean
): Flow<Boolean> {
    return data.catch { exception ->
        if (exception is IOException) { // 2
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun DataStore<Preferences>.setBoolean(key: Preferences.Key<Boolean>, value: Boolean) =
    edit { dataStore ->
        dataStore[key] = value
    }

fun DataStore<Preferences>.getLong(key: Preferences.Key<Long>, defaultValue: Long): Flow<Long> {
    return data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultValue
    }
}

suspend fun DataStore<Preferences>.setLong(key: Preferences.Key<Long>, value: Long) =
    edit { dataStore ->
        dataStore[key] = value
    }