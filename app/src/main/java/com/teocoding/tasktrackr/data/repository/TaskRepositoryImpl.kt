package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.data.local.db.dao.TaskDao
import com.teocoding.tasktrackr.data.local.db.utils.UpdateCompletedTask
import com.teocoding.tasktrackr.data.local.db.utils.UpdateTask
import com.teocoding.tasktrackr.data.mapper.toTask
import com.teocoding.tasktrackr.data.mapper.toTaskDb
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {


    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
            .mapLatest { list ->
                list.map { task ->
                    task.toTask()
                }
            }
    }

    override fun getTaskById(taskId: Long): Flow<Task?> {

        return taskDao.getTaskById(taskId)
            .mapLatest { task ->
                task?.toTask()
            }

    }


    override fun getExpiringTasks(): Flow<List<Task>> {
        return taskDao.getTasksSortByEndDate()
            .mapLatest { list ->
                list.map { task ->
                    task.toTask()
                }
            }
    }


    override fun getTaskThatTitleContains(query: String): Flow<List<Task>> {
        return taskDao.queryTasksByName(query)
            .mapLatest { list ->
                list.map { task ->
                    task.toTask()
                }
            }

    }

    override suspend fun deleteTask(taskId: Long) {

        taskDao.deleteTask(taskId)

    }

    override suspend fun insertTask(task: Task) {

        val taskDb = task.toTaskDb()

        taskDao.insertTask(taskDb)

    }

    override suspend fun updateCompleted(taskId: Long, isCompleted: Boolean) {

        val updatedTask = UpdateCompletedTask(
            id = taskId,
            isCompleted = isCompleted,
            completedDate = if (isCompleted) LocalDate.now().toEpochDay() else null
        )

        taskDao.updateCompletedTask(updatedTask)

    }

    override suspend fun updateTask(
        taskId: Long,
        title: String,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        priority: Priority,
        projectId: Long
    ) {

        val updateTask = UpdateTask(
            id = taskId,
            title = title,
            description = description,
            startDate = startDate?.toEpochDay(),
            endDate = endDate?.toEpochDay(),
            priority = priority.name,
            projectId = projectId
        )

        taskDao.updateTask(updateTask)

    }

    override fun getTaskByProject(projectId: Long): Flow<List<Task>> {
        return taskDao.getTaskByProject(projectId).mapLatest { tasks ->
            tasks.map { task ->
                task.toTask()
            }
        }
    }

    override fun getTasksThatExpireAt(date: LocalDate): Flow<List<Task>> {
        val epochDay = date.toEpochDay()

        return taskDao.getTasksThatExpireAt(epochDay)
            .map { list ->
                list.map { taskDb ->
                    taskDb.toTask()
                }
            }
    }
}