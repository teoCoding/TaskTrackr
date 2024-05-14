package com.teocoding.tasktrackr.presentation.screen.settings

sealed interface SettingsEvent {

    data class SwitchDeadlineNotificationEnabled(val isEnabled: Boolean) : SettingsEvent

    data class DeadlineNotificationTimeChanged(val hour: Int, val minute: Int) : SettingsEvent

    data class DarkModeChanged(val darkMode: Int) : SettingsEvent
}