package com.teocoding.tasktrackr.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "project",
    indices = [
        Index(value = ["title"])
    ]
)
data class ProjectDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "start_date")
    val startDate: Long?,
    @ColumnInfo(name = "end_date")
    val endDate: Long?,
    @ColumnInfo(name = "completed_date")
    val completedDate: Long?,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean
)
