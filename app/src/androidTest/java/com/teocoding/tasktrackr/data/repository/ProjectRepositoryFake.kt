package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.sqrt

class ProjectRepositoryFake @Inject constructor() : ProjectRepository {


    private val projects = MutableStateFlow(generateDummyProjects())


    override fun getAllProjects(): Flow<List<Project>> {
        return projects
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProjectById(projectId: Long): Flow<Project> {
        return projects.mapLatest { list ->
            list.first { it.id == projectId }
        }


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProjectsThatTitleContains(query: String): Flow<List<Project>> {
        return projects
            .mapLatest { list ->
                list.filter { project ->
                    project.title.contains(query, true)
                }
            }
    }

    override suspend fun deleteProject(projectId: Long) {
        projects.update { list ->
            list.filterNot { it.id == projectId }
        }
    }

    override suspend fun insertProject(project: Project) {

        projects.update { list ->
            val projectWithId = project.copy(
                id = list.maxOf { it.id ?: 0L } + 1L
            )
            list + listOf(projectWithId)
        }


    }

    override suspend fun updateCompleted(projectId: Long, isCompleted: Boolean) {
        projects.update {
            it.map { project ->
                if (project.id == projectId) {
                    project.copy(
                        isCompleted = isCompleted,
                        completedDate = if (isCompleted) LocalDate.now() else null
                    )
                } else {
                    project
                }

            }
        }
    }

    override suspend fun updateProject(
        projectId: Long,
        title: String,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ) {
        projects.update { list ->
            list.map { project ->
                if (project.id == projectId) {
                    project.copy(
                        title = title,
                        description = description,
                        startDate = startDate,
                        endDate = endDate
                    )
                } else {
                    project
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProjectsThatExpireAt(date: LocalDate): Flow<List<Project>> {
        return projects.mapLatest { list ->

            list.filter { project ->
                project.endDate?.isEqual(date) == true
            }
        }
    }

    private fun generateDummyProjects(): List<Project> {

        return (1..10).map { int ->

            Project(
                id = int.toLong(),
                title = "Project $int",
                description = "Description Project $int \nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                startDate = if (int % 2 == 0) {
                    LocalDate.now().minusDays(sqrt(int.toFloat()).toLong())
                } else {
                    LocalDate.now().plusDays(int.toLong())
                },
                endDate = if (int % 2 == 0) {
                    null
                } else {
                    LocalDate.now().plusDays(sqrt(int.toFloat()).toLong()).plusWeeks(int.toLong())
                },
                completedDate = null,
                isCompleted = false

            )


        }
    }

}