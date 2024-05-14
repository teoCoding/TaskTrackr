package com.teocoding.tasktrackr.domain.repository

import com.teocoding.tasktrackr.domain.model.Project
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ProjectRepository {

    fun getAllProjects(): Flow<List<Project>>

    fun getProjectById(projectId: Long): Flow<Project?>

    fun getProjectsThatTitleContains(query: String): Flow<List<Project>>

    suspend fun deleteProject(projectId: Long)

    suspend fun insertProject(project: Project)

    suspend fun updateCompleted(projectId: Long, isCompleted: Boolean)

    suspend fun updateProject(
        projectId: Long,
        title: String,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?
    )

    fun getProjectsThatExpireAt(date: LocalDate): Flow<List<Project>>
}