package com.teocoding.tasktrackr

import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.model.ProjectStats
import com.teocoding.tasktrackr.domain.model.ProjectWithStats
import com.teocoding.tasktrackr.domain.model.Task
import java.time.LocalDate
import kotlin.random.Random

object DummyData {

    val projects = generateDummyProjects()

    val projectsWithStats = generateDummyProjectsWithStats()

    val tasks = generateDummyTasks()


    private fun generateDummyProjects(): List<Project> {
        return (1..10).map {

            Project(
                id = it.toLong(),
                title = "Dummy project $it",
                description = "Dummy description $it",
                startDate = LocalDate.now().plusWeeks(it.toLong()),
                endDate = LocalDate.now().plusMonths(it.toLong()),
                completedDate = null,
                isCompleted = false
            )
        }

    }

    private fun generateDummyProjectsWithStats(): List<ProjectWithStats> {
        return (1..10).map {

            val totalTasks = Random.nextInt(from = 1, until = 10)
            val completedTask = Random.nextInt(totalTasks)
            val percentTaskCompleted = completedTask.toFloat() / totalTasks.toFloat()

            ProjectWithStats(
                project = Project(
                    id = it.toLong(),
                    title = "Dummy project $it",
                    description = "Dummy description $it",
                    startDate = LocalDate.now().plusWeeks(it.toLong()),
                    endDate = LocalDate.now().plusMonths(it.toLong()),
                    completedDate = null,
                    isCompleted = false
                ),
                stats = ProjectStats(
                    totalTasks = totalTasks,
                    completedTasks = completedTask,
                    percentTasksCompleted = percentTaskCompleted
                )
            )


        }
    }

    private fun generateDummyTasks(): List<Task> {
        return (1..10).map {

            Task(
                id = it.toLong(),
                title = "Dummy task $it",
                description = "Dummy description $it",
                startDate = LocalDate.now().plusWeeks(it.toLong()),
                endDate = LocalDate.now().plusMonths(it.toLong()),
                completedDate = null,
                isCompleted = false,
                priority = Priority.entries[Random.nextInt(0, 2)],
                projectId = it.toLong()
            )
        }

    }

}