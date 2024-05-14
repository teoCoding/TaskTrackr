package com.teocoding.tasktrackr.domain.repository

import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    fun getTaskById(taskId: Long): Flow<Task?>

    fun getExpiringTasks(): Flow<List<Task>>

    fun getTaskThatTitleContains(query: String): Flow<List<Task>>

    suspend fun deleteTask(taskId: Long)

    suspend fun insertTask(task: Task)

    suspend fun updateCompleted(taskId: Long, isCompleted: Boolean)

    suspend fun updateTask(
        taskId: Long,
        title: String,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        priority: Priority,
        projectId: Long
    )

    fun getTaskByProject(projectId: Long): Flow<List<Task>>

    fun getTasksThatExpireAt(date: LocalDate): Flow<List<Task>>
}