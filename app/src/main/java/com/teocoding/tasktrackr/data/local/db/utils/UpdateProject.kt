package com.teocoding.tasktrackr.data.local.db.utils

import androidx.room.ColumnInfo

data class UpdateProject(
    val id: Long,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "start_date")
    val startDate: Long?,
    @ColumnInfo(name = "end_date")
    val endDate: Long?
)
