package com.teocoding.tasktrackr.data.di

import com.teocoding.tasktrackr.data.repository.NotificationsRepositoryImpl
import com.teocoding.tasktrackr.data.repository.PhotoRepositoryImpl
import com.teocoding.tasktrackr.data.repository.ProjectRepositoryImpl
import com.teocoding.tasktrackr.data.repository.SettingsRepositoryImpl
import com.teocoding.tasktrackr.data.repository.TaskRepositoryImpl
import com.teocoding.tasktrackr.domain.repository.NotificationsRepository
import com.teocoding.tasktrackr.domain.repository.PhotoRepository
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.SettingsRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBinds {

    @Binds
    @Singleton
    abstract fun bindsProjectRepository(projectRepositoryImpl: ProjectRepositoryImpl): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindsTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindsPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationsRepository(
        notificationsRepositoryImpl: NotificationsRepositoryImpl
    ): NotificationsRepository

    @Binds
    @Singleton
    abstract fun bindsSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}