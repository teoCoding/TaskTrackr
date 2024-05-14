package com.teocoding.tasktrackr.data.mapper

import com.teocoding.tasktrackr.data.local.db.model.ProjectDb
import com.teocoding.tasktrackr.domain.model.Project
import java.time.LocalDate

fun ProjectDb.toProject(): Project {

    val startDate = this.startDate?.let { LocalDate.ofEpochDay(it) }
    val endDate = this.endDate?.let { LocalDate.ofEpochDay(it) }
    val completedDate = this.completedDate?.let { LocalDate.ofEpochDay(it) }

    return Project(
        id = this.id,
        title = this.title,
        description = this.description,
        startDate = startDate,
        endDate = endDate,
        completedDate = completedDate,
        isCompleted = this.isCompleted
    )
}

fun Project.toProjectDb(): ProjectDb {

    val startDate = this.startDate?.toEpochDay()
    val endDate = this.endDate?.toEpochDay()
    val completedDate = this.completedDate?.toEpochDay()

    return ProjectDb(
        id = this.id,
        title = this.title,
        description = this.description,
        startDate = startDate,
        endDate = endDate,
        completedDate = completedDate,
        isCompleted = isCompleted
    )
}