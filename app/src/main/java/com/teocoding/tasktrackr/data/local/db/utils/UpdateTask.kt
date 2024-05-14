package com.teocoding.tasktrackr.data.local.db.utils

import androidx.room.ColumnInfo

data class UpdateTask(
    val id: Long,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "start_date")
    val startDate: Long?,
    @ColumnInfo(name = "end_date")
    val endDate: Long?,
    @ColumnInfo(name = "priority")
    val priority: String?,
    @ColumnInfo(name = "project_id")
    val projectId: Long
)
