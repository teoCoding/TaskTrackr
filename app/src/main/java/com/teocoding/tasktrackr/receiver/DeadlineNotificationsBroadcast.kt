package com.teocoding.tasktrackr.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.teocoding.tasktrackr.domain.repository.NotificationsRepository
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import com.teocoding.tasktrackr.presentation.screen.settings.NotificationsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class DeadlineNotificationsBroadcast : BroadcastReceiver() {

    @Inject
    lateinit var notificationsRepository: NotificationsRepository

    @Inject
    lateinit var projectRepository: ProjectRepository

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var notificationsManager: NotificationsManager


    override fun onReceive(context: Context, intent: Intent) {

        CoroutineScope(SupervisorJob()).launch {

            val areNotificationsEnabled =
                notificationsRepository.isDeadlineNotificationEnabled().first()
                        && notificationsManager.areNotificationsEnabled()

            if (areNotificationsEnabled) {

                val today = LocalDate.now()

                val expiringTasks = taskRepository.getTasksThatExpireAt(today).first()

                if (expiringTasks.isNotEmpty()) {

                    expiringTasks.forEach { task ->
                        notificationsManager.sendTaskDeadlineNotifications(task)
                    }
                }

                val expiringProjects = projectRepository.getProjectsThatExpireAt(today).first()

                if (expiringProjects.isNotEmpty()) {

                    expiringProjects.forEach { project ->
                        notificationsManager.sendProjectDeadlineNotifications(project)
                    }
                }

                val notificationTime =
                    notificationsRepository.getDeadlineNotificationsTime().first()

                notificationsManager.setDeadlineReminderAlarm(notificationTime)

            }
        }

    }

}