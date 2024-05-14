package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

class TaskRepositoryFake @Inject constructor() : TaskRepository {


    private val tasks = MutableStateFlow(generateDummyTasks())


    override fun getAllTasks(): Flow<List<Task>> {
        return tasks
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTaskById(taskId: Long): Flow<Task> {

        return tasks
            .mapLatest { list ->
                list.first { it.id == taskId }
            }
            .distinctUntilChanged()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getExpiringTasks(): Flow<List<Task>> {
        return tasks
            .mapLatest { list ->

                list.sortedWith(
                    compareBy(nullsLast()) { task ->
                        task.endDate
                    }
                )

            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTaskThatTitleContains(query: String): Flow<List<Task>> {
        return tasks
            .mapLatest { list ->
                list.filter { task ->
                    task.title.contains(query, true)
                }
            }
    }

    override suspend fun deleteTask(taskId: Long) {
        tasks.update { list ->
            list.filter { it.id != taskId }
        }


    }

    override suspend fun insertTask(task: Task) {
        tasks.update { list ->
            val taskWithId = task.copy(
                id = list.maxOf { it.id ?: 0L } + 1L
            )
            list + listOf(taskWithId)
        }
    }


    override suspend fun updateCompleted(taskId: Long, isCompleted: Boolean) {

        tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    task.copy(
                        isCompleted = isCompleted,
                        completedDate = if (isCompleted) LocalDate.now() else null
                    )
                } else {
                    task
                }
            }
        }

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
        tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    task.copy(
                        title = title,
                        description = description,
                        startDate = startDate,
                        endDate = endDate,
                        priority = priority
                    )
                } else {
                    task
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTaskByProject(projectId: Long): Flow<List<Task>> {
        return tasks.mapLatest { list ->
            list.filter { task -> task.projectId == projectId }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTasksThatExpireAt(date: LocalDate): Flow<List<Task>> {
        return tasks.mapLatest { list ->

            list.filter { task ->
                task.endDate?.isEqual(date) == true
            }
        }
    }

    private fun generateDummyTasks(): List<Task> {

        return (1..30).map { int ->

            val completed = Random.nextBoolean()
            val completedDate = if (completed) {
                LocalDate.now()
            } else {
                null
            }

            Task(
                id = int.toLong(),
                title = "Task $int",
                description = "Description Task $int \nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
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
                completedDate = completedDate,
                isCompleted = completed,
                priority = Priority.entries[Random.nextInt(0, 2)],
                projectId = Random.nextInt(1..10).toLong()

            )
        }
    }


}