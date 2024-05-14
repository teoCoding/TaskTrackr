package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.data.local.db.dao.ProjectDao
import com.teocoding.tasktrackr.data.local.db.utils.UpdateCompletedProject
import com.teocoding.tasktrackr.data.local.db.utils.UpdateProject
import com.teocoding.tasktrackr.data.mapper.toProject
import com.teocoding.tasktrackr.data.mapper.toProjectDb
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
) : ProjectRepository {


    override fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects().mapLatest { list ->
            list.map { project ->
                project.toProject()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProjectById(projectId: Long): Flow<Project?> {
        return projectDao.getProjectById(projectId)
            .mapLatest { project ->
                project?.toProject()
            }
    }


    override fun getProjectsThatTitleContains(query: String): Flow<List<Project>> {

        return projectDao.queryProjectsByName(query)
            .mapLatest { list ->
                list.map { project ->
                    project.toProject()
                }
            }
    }

    override suspend fun deleteProject(projectId: Long) {

        projectDao.deleteProject(projectId)
    }

    override suspend fun insertProject(project: Project) {

        val projectDb = project.toProjectDb()

        projectDao.insertProject(projectDb)

    }

    override suspend fun updateCompleted(projectId: Long, isCompleted: Boolean) {

        val updateProject = UpdateCompletedProject(
            id = projectId,
            isCompleted = isCompleted,
            completedDate = if (isCompleted) LocalDate.now().toEpochDay() else null
        )

        projectDao.updateCompletedProject(updateProject)
    }

    override suspend fun updateProject(
        projectId: Long,
        title: String,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    ) {
        val updateProject = UpdateProject(
            id = projectId,
            title = title,
            description = description,
            startDate = startDate?.toEpochDay(),
            endDate = endDate?.toEpochDay(),
        )

        projectDao.updateProject(updateProject)
    }

    override fun getProjectsThatExpireAt(date: LocalDate): Flow<List<Project>> {
        val epochDay = date.toEpochDay()

        return projectDao.getProjectsThatExpireAt(epochDay)
            .map { list ->
                list.map { projectDb ->
                    projectDb.toProject()
                }
            }
    }
}