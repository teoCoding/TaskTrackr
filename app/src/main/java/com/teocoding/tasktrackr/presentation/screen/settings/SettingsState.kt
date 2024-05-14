package com.teocoding.tasktrackr.presentation.screen.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Immutable
import java.time.LocalTime

@Immutable
data class SettingsState(
    val deadLineNotificationEnabled: Boolean = false,
    val deadlineNotificationTime: LocalTime = LocalTime.of(8, 0),
    val darkMode: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
)