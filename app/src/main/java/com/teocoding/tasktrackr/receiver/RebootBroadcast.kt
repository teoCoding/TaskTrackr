package com.teocoding.tasktrackr.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.teocoding.tasktrackr.domain.repository.NotificationsRepository
import com.teocoding.tasktrackr.presentation.screen.settings.NotificationsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RebootBroadcast : BroadcastReceiver() {

    @Inject
    lateinit var notificationsManager: NotificationsManager

    @Inject
    lateinit var notificationsRepository: NotificationsRepository

    override fun onReceive(context: Context, intent: Intent) {


        CoroutineScope(SupervisorJob()).launch {

            val isRebootIntent = intent.action == "android.intent.action.BOOT_COMPLETED"
                    || intent.action == "android.intent.action.MY_PACKAGE_REPLACED"
                    || intent.action == "android.intent.action.QUICKBOOT_POWERON"

            val areNotificationsEnabled =
                notificationsRepository.isDeadlineNotificationEnabled().first()
                        && notificationsManager.areNotificationsEnabled()

            if (isRebootIntent && areNotificationsEnabled) {

                val timeSet = notificationsRepository.getDeadlineNotificationsTime().first()
                notificationsManager.setDeadlineReminderAlarm(timeSet)

            }

        }

    }

}