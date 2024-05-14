package com.teocoding.tasktrackr.presentation.screen.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.presentation.MainActivity
import com.teocoding.tasktrackr.receiver.DeadlineNotificationsBroadcast
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class NotificationsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    fun createDeadlineNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notifications_channel_deadline)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(NOTIFICATIONS_CHANNEL_DEADLINE_ID, name, importance).apply {
                    this.enableVibration(true)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun sendProjectDeadlineNotifications(project: Project) {

        val requestId = NOTIFICATION_DEADLINE_PROJECT_ID + (project.id?.toInt() ?: 1)

        val projectDetailIntent = Intent(
            Intent.ACTION_VIEW,
            ProjectRoute.Details.createDeepLink(project.id!!).toUri(),
            context,
            MainActivity::class.java
        )

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(projectDetailIntent)
            getPendingIntent(requestId, PendingIntent.FLAG_IMMUTABLE)
        }

        val contextForLanguage = ContextCompat.getContextForLanguage(context)

        val title = contextForLanguage.getString(R.string.deadline_project_notification_title)

        val notificationsBuilder = getDefaultNotificationsBuilder()
            .setContentTitle(title)
            .setContentText(project.title)
            .setContentIntent(pendingIntent)

        notificationManager.notify(requestId, notificationsBuilder.build())
    }

    @SuppressLint("MissingPermission")
    fun sendTaskDeadlineNotifications(task: Task) {

        val requestId = NOTIFICATION_DEADLINE_TASK_ID + (task.id?.toInt() ?: 1)

        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            TaskRoute.Details.createDeepLink(task.id!!).toUri(),
            context,
            MainActivity::class.java
        )

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(requestId, PendingIntent.FLAG_IMMUTABLE)
        }

        val contextForLanguage = ContextCompat.getContextForLanguage(context)

        val title = contextForLanguage.getString(R.string.deadline_task_notification_title)

        val notificationsBuilder = getDefaultNotificationsBuilder()
            .setContentTitle(title)
            .setContentText(task.title)
            .setContentIntent(pendingIntent)

        notificationManager.notify(requestId, notificationsBuilder.build())
    }

    private fun getDefaultNotificationsBuilder(): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_DEADLINE_ID)
            .setSmallIcon(R.drawable.ic_task_24dp)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setAutoCancel(true)
    }


    fun areNotificationsEnabled(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED

        }

        return notificationManager.areNotificationsEnabled()
    }


    fun setDeadlineReminderAlarm(notificationTime: LocalTime) {

        val pendingIntent = getDeadlineAlarmPendingIntent()

        val millisecondsNotification = calculateNextAlarmTime(notificationTime)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            millisecondsNotification,
            pendingIntent
        )
    }

    fun disableDeadlineReminderAlarm() {
        val pendingIntent = getDeadlineAlarmPendingIntent()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)

    }

    private fun getDeadlineAlarmPendingIntent(): PendingIntent {

        val intent = Intent(context, DeadlineNotificationsBroadcast::class.java)

        val pendingIntentFlag =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE


        return PendingIntent.getBroadcast(
            context, PENDING_INTENT_REQUEST_CODE_DEADLINE_ALARM_ID, intent,
            pendingIntentFlag
        )
    }

    private fun calculateNextAlarmTime(notificationTime: LocalTime): Long {

        val nowTime = LocalTime.now()

        val localDate = if (nowTime.isAfter(notificationTime)) {
            LocalDate.now().plusDays(1)
        } else LocalDate.now()

        val zoneDateTime = LocalDateTime.of(localDate, notificationTime)
            .atZone(ZoneId.systemDefault())

        return zoneDateTime.toInstant().toEpochMilli()

    }


    companion object {
        private const val NOTIFICATIONS_CHANNEL_DEADLINE_ID = "deadline_notification_channel"
        private const val NOTIFICATION_DEADLINE_PROJECT_ID = 1001
        private const val NOTIFICATION_DEADLINE_TASK_ID = 2001
        private const val PENDING_INTENT_REQUEST_CODE_DEADLINE_ALARM_ID = 3001
    }
}