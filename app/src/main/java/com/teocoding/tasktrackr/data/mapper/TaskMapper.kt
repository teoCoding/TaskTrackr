package com.teocoding.tasktrackr.data.mapper

import com.teocoding.tasktrackr.data.local.db.model.TaskDb
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Task
import java.time.LocalDate


fun TaskDb.toTask(): Task {

    val startDate = this.startDate?.let { LocalDate.ofEpochDay(it) }
    val endDate = this.endDate?.let { LocalDate.ofEpochDay(it) }
    val completedDate = this.completedDate?.let { LocalDate.ofEpochDay(it) }
    val priority = this.priority?.let { Priority.valueOf(it) } ?: Priority.Low

    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        startDate = startDate,
        endDate = endDate,
        priority = priority,
        completedDate = completedDate,
        isCompleted = this.isCompleted,
        projectId = this.projectId
    )
}

fun Task.toTaskDb(): TaskDb {

    val startDate = this.startDate?.toEpochDay()
    val endDate = this.endDate?.toEpochDay()
    val completedDate = this.completedDate?.toEpochDay()

    val priorityName = this.priority.name

    return TaskDb(
        id = this.id,
        title = this.title,
        description = this.description,
        startDate = startDate,
        endDate = endDate,
        completedDate = completedDate,
        priority = priorityName,
        isCompleted = isCompleted,
        projectId = projectId
    )
}