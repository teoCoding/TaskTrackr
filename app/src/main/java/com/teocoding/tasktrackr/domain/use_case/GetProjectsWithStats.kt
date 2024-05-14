package com.teocoding.tasktrackr.domain.use_case

import com.teocoding.tasktrackr.domain.model.ProjectWithStats
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetProjectsWithStats @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val projectStatsCalculator: ProjectStatsCalculator
) {

    fun execute(): Flow<List<ProjectWithStats>> {

        return projectRepository.getAllProjects()
            .combine(taskRepository.getAllTasks()) { projects, tasks ->

                val tasksMap = tasks.groupBy { task ->
                    task.projectId
                }

                val projectsMap = projects.associateWith { project ->
                    tasksMap[project.id]
                }

                projectsMap.map { (project, tasks) ->

                    val stats = projectStatsCalculator.execute(tasks)

                    ProjectWithStats(
                        project = project,
                        stats = stats

                    )
                }
                    .sortedWith(compareBy(nullsLast()) { projectWithStats ->

                        val project = projectWithStats.project

                        project.endDate
                    }

                    )
            }
    }

}