package com.teocoding.tasktrackr.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.ProjectStats
import com.teocoding.tasktrackr.domain.model.Task
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random
import kotlin.random.nextInt

class ProjectStatsCalculatorTest {


    private val projectStatsCalculator = ProjectStatsCalculator()

    @Test
    fun `calculate stats when tasks is null return success`() {

        val tasks = null

        val expected = ProjectStats(
            totalTasks = 0,
            completedTasks = 0,
            percentTasksCompleted = 0f
        )

        val result = projectStatsCalculator.execute(tasks)


        assertThat(result).isEqualTo(expected)


    }

    @Test
    fun `calculate stats when tasks is empty return success`() {

        val tasks = emptyList<Task>()

        val expected = ProjectStats(
            totalTasks = 0,
            completedTasks = 0,
            percentTasksCompleted = 0f
        )

        val result = projectStatsCalculator.execute(tasks)


        assertThat(result).isEqualTo(expected)

    }

    @Test
    fun `calculate stats when tasks is not empty return success`() {

        val tasks = listOf(
            Task(
                id = 1,
                title = "Task 1",
                description = "Description Task 1 \nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                startDate = LocalDate.now().minusDays(2),
                endDate = null,
                completedDate = LocalDate.now(),
                isCompleted = true,
                priority = Priority.entries[Random.nextInt(0, 2)],
                projectId = Random.nextInt(1..10).toLong()

            ),

            Task(
                id = 2,
                title = "Task 2",
                description = "Description Task 2 \nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                startDate = LocalDate.now().minusDays(2),
                endDate = null,
                completedDate = LocalDate.now(),
                isCompleted = true,
                priority = Priority.entries[Random.nextInt(0, 2)],
                projectId = Random.nextInt(1..10).toLong()

            ),

            Task(
                id = 3,
                title = "Task 3",
                description = "Description Task 3 \nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                startDate = LocalDate.now().minusDays(2),
                endDate = null,
                completedDate = null,
                isCompleted = false,
                priority = Priority.entries[Random.nextInt(0, 2)],
                projectId = Random.nextInt(1..10).toLong()

            ),

            Task(
                id = 4,
                title = "Task 4",
                description = "Description Task 4 \nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                startDate = LocalDate.now().minusDays(2),
                endDate = null,
                completedDate = LocalDate.now(),
                isCompleted = true,
                priority = Priority.entries[Random.nextInt(0, 2)],
                projectId = Random.nextInt(1..10).toLong()

            )
        )

        val expected = ProjectStats(
            totalTasks = 4,
            completedTasks = 3,
            percentTasksCompleted = 0.75f
        )

        val result = projectStatsCalculator.execute(tasks)


        assertThat(result).isEqualTo(expected)

    }
}