package com.teocoding.tasktrackr.data.local.db.utils

import androidx.room.ColumnInfo

data class UpdateCompletedProject(
    val id: Long,
    @ColumnInfo(name = "completed_date")
    val completedDate: Long?,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
)