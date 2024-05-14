package com.teocoding.tasktrackr.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface NotificationsRepository {

    fun isDeadlineNotificationEnabled(): Flow<Boolean>

    suspend fun setNotificationInAdvanceEnabled(isEnabled: Boolean)

    fun getDeadlineNotificationsTime(): Flow<LocalTime>

    suspend fun setDeadlineNotificationsTime(time: LocalTime)
}