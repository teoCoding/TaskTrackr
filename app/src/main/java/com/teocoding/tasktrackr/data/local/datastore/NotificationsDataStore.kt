package com.teocoding.tasktrackr.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.teocoding.tasktrackr.data.local.datastore.NotificationsDataStore.Companion.NOTIFICATIONS_PREFERENCES
import com.teocoding.tasktrackr.data.utils.getBoolean
import com.teocoding.tasktrackr.data.utils.getString
import com.teocoding.tasktrackr.data.utils.setBoolean
import com.teocoding.tasktrackr.data.utils.setString
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.notificationsDataStore: DataStore<Preferences> by preferencesDataStore(name = NOTIFICATIONS_PREFERENCES)

@Singleton
class NotificationsDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val NOTIFICATIONS_PREFERENCES = "notifications_preferences"

        val DEADLINE_NOTIFICATIONS = booleanPreferencesKey("deadline_notifications")
        val DEADLINE_NOTIFICATIONS_TIME = stringPreferencesKey("deadline_notifications_time")
    }

    private val notificationsDataStore = context.notificationsDataStore


    val areDeadlineNotificationsEnabled = notificationsDataStore.getBoolean(
        key = DEADLINE_NOTIFICATIONS,
        defaultValue = false
    )

    val deadlineNotificationsTime = notificationsDataStore.getString(
        key = DEADLINE_NOTIFICATIONS_TIME,
        defaultValue = null
    )

    suspend fun setDeadlineNotificationsEnabled(value: Boolean) =
        notificationsDataStore.setBoolean(DEADLINE_NOTIFICATIONS, value)

    suspend fun setDeadlineNotificationsTime(value: String) =
        notificationsDataStore.setString(DEADLINE_NOTIFICATIONS_TIME, value)

}
