package com.teocoding.tasktrackr

import android.app.Application
import com.teocoding.tasktrackr.presentation.screen.settings.NotificationsManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TaskTrackrApp : Application() {


    @Inject
    lateinit var notificationManager: NotificationsManager

    override fun onCreate() {
        super.onCreate()

        notificationManager.createDeadlineNotificationsChannel()
    }
}