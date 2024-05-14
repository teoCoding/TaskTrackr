package com.teocoding.tasktrackr.domain.use_case

import com.teocoding.tasktrackr.domain.model.ProjectStats
import com.teocoding.tasktrackr.domain.model.Task
import javax.inject.Inject

class ProjectStatsCalculator @Inject constructor() {


    fun execute(tasks: List<Task>?): ProjectStats {

        val totalTasks = tasks?.size ?: 0
        val completedTask = tasks?.filter { it.isCompleted }?.size ?: 0
        val percentsCompleted = if (totalTasks == 0) {
            0f
        } else {
            completedTask.toFloat() / totalTasks.toFloat()
        }

        return ProjectStats(
            totalTasks = totalTasks,
            completedTasks = completedTask,
            percentTasksCompleted = percentsCompleted
        )
    }
}