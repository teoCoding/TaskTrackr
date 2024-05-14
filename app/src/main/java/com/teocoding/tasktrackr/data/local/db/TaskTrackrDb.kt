package com.teocoding.tasktrackr.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teocoding.tasktrackr.data.local.db.dao.PhotoDao
import com.teocoding.tasktrackr.data.local.db.dao.ProjectDao
import com.teocoding.tasktrackr.data.local.db.dao.TaskDao
import com.teocoding.tasktrackr.data.local.db.model.PhotoDb
import com.teocoding.tasktrackr.data.local.db.model.ProjectDb
import com.teocoding.tasktrackr.data.local.db.model.TaskDb

@Database(
    entities = [
        ProjectDb::class, TaskDb::class, PhotoDb::class
    ],
    version = 1,
    exportSchema = true
)
abstract class TaskTrackrDb : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun photoDao(): PhotoDao

}