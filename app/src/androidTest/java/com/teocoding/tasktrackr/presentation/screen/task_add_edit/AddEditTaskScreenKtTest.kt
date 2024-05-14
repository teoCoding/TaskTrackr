package com.teocoding.tasktrackr.presentation.screen.task_add_edit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.data.repository.ProjectRepositoryFake
import com.teocoding.tasktrackr.data.repository.TaskRepositoryFake
import com.teocoding.tasktrackr.domain.InsertTaskValidator
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddEditTaskScreenKtTest {


    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testAddTaskBehavior() {

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

        val viewModel = AddEditTaskViewModel(
            savedStateHandle = SavedStateHandle(),
            projectRepository = ProjectRepositoryFake(),
            taskRepository = TaskRepositoryFake(),
            insertTaskValidator = InsertTaskValidator()
        )

        composeTestRule.setContent {
            TaskTrackrTheme {

                val state by viewModel.screenState.collectAsStateWithLifecycle()

                AddEditTaskScreen(
                    contentPaddingValues = PaddingValues(0.dp),
                    onGoBack = { },
                    onEvent = viewModel::onEvent,
                    screenState = state
                )
            }

        }

        composeTestRule.onRoot().printToLog("AddEditTaskScreenKtTest")

        // Test Project Selector
        val projectSelectorText = targetContext.getString(R.string.choose_project)

        composeTestRule.onNodeWithText(projectSelectorText)
            .performClick()

        composeTestRule.onNodeWithText("Project 2")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Project 1")
            .performClick()

        composeTestRule.onNodeWithText("Project 2")
            .assertIsNotDisplayed()

        val selectedProjectString = targetContext.getString(R.string.project_caption, "Project 1")

        composeTestRule.onNodeWithText(selectedProjectString)
            .assertExists()


        // Test Title TextField
        val titleContentDescription = targetContext.getString(
            R.string.cd_insert_title
        )

        composeTestRule.onNodeWithContentDescription(
            label = titleContentDescription
        )
            .performTextInput("Title")

        composeTestRule.onNodeWithContentDescription(
            label = titleContentDescription
        )
            .assertTextEquals("Title")


        // Test Description TextField
        val descriptionContentDescription = targetContext.getString(
            R.string.cd_insert_description
        )

        composeTestRule.onNodeWithContentDescription(descriptionContentDescription)
            .performTextInput("Description")

        composeTestRule.onNodeWithContentDescription(descriptionContentDescription)
            .assertTextEquals("Description")


        // Test Date Picker
        val startDateText = targetContext.getString(R.string.start_date)

        composeTestRule.onNodeWithText(startDateText)
            .performClick()

        composeTestRule.onNode(isDialog())
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(targetContext.getString(R.string.ok))
            .performClick()

        composeTestRule.onNode(isDialog())
            .isNotDisplayed()


        // Test Use End Date Switch
        composeTestRule.onNode(isToggleable())
            .performClick()

        val selectEndDateText = targetContext.getString(R.string.select_date)

        composeTestRule.onNodeWithText(selectEndDateText)
            .isDisplayed()

        // Test Priority Chips
        val mediumPriorityText = targetContext.getString(R.string.medium)

        composeTestRule.onNodeWithText(mediumPriorityText)
            .assertIsSelectable()
            .performClick()

        composeTestRule.onNodeWithText(mediumPriorityText)
            .assertIsSelected()


    }


    @Test
    fun testEditTaskBehavior() = runTest {

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

        val taskRepository = TaskRepositoryFake()
        val projectRepository = ProjectRepositoryFake()
        val taskId = 1L

        val expectedTask = taskRepository.getTaskById(taskId).first()
        val expectedProject = projectRepository.getProjectById(expectedTask.projectId).first()

        val viewModel = AddEditTaskViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TaskRoute.AddEditTask.TASK_ID to taskId)),
            projectRepository = projectRepository,
            taskRepository = taskRepository,
            insertTaskValidator = InsertTaskValidator()
        )

        composeTestRule.setContent {
            TaskTrackrTheme {

                val state by viewModel.screenState.collectAsStateWithLifecycle()

                AddEditTaskScreen(
                    contentPaddingValues = PaddingValues(0.dp),
                    onGoBack = { },
                    onEvent = viewModel::onEvent,
                    screenState = state
                )
            }

        }

        composeTestRule.onRoot().printToLog("AddEditTaskScreenKtTest")


        // Test Project Selector
        val selectedProject = expectedProject.title


        val selectedProjectString =
            targetContext.getString(R.string.project_caption, selectedProject)

        composeTestRule.onNodeWithText(selectedProjectString)
            .assertExists()


        // Test Title TextField
        val titleContentDescription = targetContext.getString(
            R.string.cd_insert_title
        )

        val taskTitle = expectedTask.title

        composeTestRule.onNodeWithContentDescription(
            label = titleContentDescription
        )
            .assertTextEquals(taskTitle)


        // Test Description TextField
        val descriptionContentDescription = targetContext.getString(
            R.string.cd_insert_description
        )

        val taskDescription = expectedTask.description ?: ""


        composeTestRule.onNodeWithContentDescription(descriptionContentDescription)
            .assertTextEquals(taskDescription)


        // Test Date Selector
        val dateFormat = targetContext.getCurrentLocale()
            .getShortStyleFormatter()

        val startDateText = expectedTask.startDate?.format(dateFormat)
        val endDateText = expectedTask.endDate?.format(dateFormat)

        startDateText?.let { date ->
            composeTestRule.onNodeWithText(date)
                .assertIsDisplayed()
        }

        endDateText?.let { date ->
            composeTestRule.onNodeWithText(date)
                .assertIsDisplayed()

            composeTestRule.onNode(isToggleable())
                .assertIsOn()
        } ?: {
            composeTestRule.onNode(isToggleable())
                .assertIsOff()
        }


        // Test Priority Chips
        val priorityText = when (expectedTask.priority) {
            com.teocoding.tasktrackr.domain.Priority.Low -> targetContext.getString(R.string.low)
            com.teocoding.tasktrackr.domain.Priority.Medium -> targetContext.getString(R.string.medium)
            com.teocoding.tasktrackr.domain.Priority.High -> targetContext.getString(R.string.high)
        }

        composeTestRule.onNodeWithText(priorityText)
            .assertIsSelected()


    }

}