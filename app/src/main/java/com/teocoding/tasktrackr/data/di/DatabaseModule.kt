package com.teocoding.tasktrackr.data.di

import android.content.Context
import androidx.room.Room
import com.teocoding.tasktrackr.data.local.db.TaskTrackrDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TaskTrackrDb::class.java, "task_trackr.db"
    ).build()


    @Provides
    @Singleton
    fun provideProjectDao(db: TaskTrackrDb) = db.projectDao()


    @Provides
    @Singleton
    fun provideTaskDao(db: TaskTrackrDb) = db.taskDao()


    @Provides
    @Singleton
    fun providePhotoDao(db: TaskTrackrDb) = db.photoDao()


}