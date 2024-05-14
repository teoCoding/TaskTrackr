package com.teocoding.tasktrackr.domain.model

data class ProjectStats(
    val totalTasks: Int,
    val completedTasks: Int,
    val percentTasksCompleted: Float
)
