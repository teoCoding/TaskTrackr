package com.teocoding.tasktrackr.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "task",
    indices = [
        Index(value = ["project_id"]),
        Index(value = ["title"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProjectDb::class, parentColumns = ["id"], childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskDb(
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
    @ColumnInfo(name = "priority")
    val priority: String?,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    @ColumnInfo(name = "project_id")
    val projectId: Long
)