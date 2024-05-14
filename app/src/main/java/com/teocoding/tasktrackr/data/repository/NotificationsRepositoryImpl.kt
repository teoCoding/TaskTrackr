package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.data.local.datastore.NotificationsDataStore
import com.teocoding.tasktrackr.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val notificationsDataStore: NotificationsDataStore
) : NotificationsRepository {

    private val timeFormatter = DateTimeFormatter.ISO_TIME.withLocale(Locale.US)

    override fun isDeadlineNotificationEnabled(): Flow<Boolean> {
        return notificationsDataStore.areDeadlineNotificationsEnabled
    }

    override suspend fun setNotificationInAdvanceEnabled(isEnabled: Boolean) {

        notificationsDataStore.setDeadlineNotificationsEnabled(isEnabled)

    }

    override fun getDeadlineNotificationsTime(): Flow<LocalTime> {
        return notificationsDataStore.deadlineNotificationsTime
            .map { time ->
                time?.let { timeNotNull ->
                    LocalTime.parse(
                        timeNotNull,
                        timeFormatter
                    )
                } ?: LocalTime.of(8, 0)
            }
    }

    override suspend fun setDeadlineNotificationsTime(time: LocalTime) {
        val timeString = time.format(timeFormatter)

        notificationsDataStore.setDeadlineNotificationsTime(timeString)
    }
}