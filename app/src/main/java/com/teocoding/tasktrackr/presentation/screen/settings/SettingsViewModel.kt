package com.teocoding.tasktrackr.presentation.screen.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.repository.NotificationsRepository
import com.teocoding.tasktrackr.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
    private val notificationsManager: NotificationsManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(SettingsState())
    val screenState: StateFlow<SettingsState> = _screenState.asStateFlow()

    init {

        val deadlineNotificationEnabled = notificationsRepository.isDeadlineNotificationEnabled()
        val deadlineNotificationTime = notificationsRepository.getDeadlineNotificationsTime()
        val selectedDarkMode = settingsRepository.getDarkMode()

        combine(
            deadlineNotificationEnabled,
            deadlineNotificationTime,
            selectedDarkMode
        ) { deadLineEnabled, deadlineTime, darkMode ->
            _screenState.update { state ->
                state.copy(
                    deadLineNotificationEnabled = deadLineEnabled,
                    deadlineNotificationTime = deadlineTime,
                    darkMode = darkMode
                )
            }

        }.launchIn(viewModelScope)

    }

    fun onEvent(event: SettingsEvent) {
        when (event) {

            is SettingsEvent.SwitchDeadlineNotificationEnabled -> {

                switchDeadlineNotificationEnabled(event.isEnabled)
            }

            is SettingsEvent.DeadlineNotificationTimeChanged -> {
                onDeadlineNotificationTimeChanged(
                    hour = event.hour,
                    minute = event.minute
                )
            }

            is SettingsEvent.DarkModeChanged -> {
                viewModelScope.launch {
                    settingsRepository.setDarkMode(event.darkMode)
                    AppCompatDelegate.setDefaultNightMode(event.darkMode)
                }
            }
        }
    }

    private fun switchDeadlineNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {

            notificationsRepository.setNotificationInAdvanceEnabled(enabled)

            if (enabled) {
                notificationsManager.setDeadlineReminderAlarm(_screenState.value.deadlineNotificationTime)
            } else {
                notificationsManager.disableDeadlineReminderAlarm()
            }
        }
    }

    private fun onDeadlineNotificationTimeChanged(hour: Int, minute: Int) {

        viewModelScope.launch {

            val notificationsTime = LocalTime.of(hour, minute)

            notificationsRepository.setDeadlineNotificationsTime(notificationsTime)

            notificationsManager.setDeadlineReminderAlarm(notificationsTime)
        }
    }

}