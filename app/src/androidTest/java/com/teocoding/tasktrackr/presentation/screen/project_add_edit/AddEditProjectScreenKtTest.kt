package com.teocoding.tasktrackr.presentation.screen.project_add_edit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
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
import com.teocoding.tasktrackr.domain.InsertProjectValidator
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddEditProjectScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAddProjectBehavior() {

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

        val viewModel = AddEditProjectViewModel(
            savedStateHandle = SavedStateHandle(),
            projectRepository = ProjectRepositoryFake(),
            insertProjectValidator = InsertProjectValidator()
        )

        composeTestRule.setContent {
            TaskTrackrTheme {

                val state by viewModel.screenState.collectAsStateWithLifecycle()

                AddEditProjectScreen(
                    contentPaddingValues = PaddingValues(0.dp),
                    onGoBack = { },
                    onEvent = viewModel::onEvent,
                    screenState = state
                )
            }

        }

        composeTestRule.onRoot().printToLog("AddEditProjectScreenKtTest")


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


    }

    @Test
    fun testEditTaskBehavior() = runTest {

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

        val projectRepository = ProjectRepositoryFake()
        val projectId = 1L

        val expectedProject = projectRepository.getProjectById(projectId).first()

        val viewModel = AddEditProjectViewModel(
            savedStateHandle = SavedStateHandle(mapOf(ProjectRoute.AddEditProject.PROJECT_ID to projectId)),
            projectRepository = projectRepository,
            insertProjectValidator = InsertProjectValidator()
        )

        composeTestRule.setContent {
            TaskTrackrTheme {

                val state by viewModel.screenState.collectAsStateWithLifecycle()

                AddEditProjectScreen(
                    contentPaddingValues = PaddingValues(0.dp),
                    onGoBack = { },
                    onEvent = viewModel::onEvent,
                    screenState = state
                )
            }

        }

        composeTestRule.onRoot().printToLog("AddEditTaskScreenKtTest")


        // Test Title TextField
        val titleContentDescription = targetContext.getString(
            R.string.cd_insert_title
        )

        val taskTitle = expectedProject.title

        composeTestRule.onNodeWithContentDescription(
            label = titleContentDescription
        )
            .assertTextEquals(taskTitle)


        // Test Description TextField
        val descriptionContentDescription = targetContext.getString(
            R.string.cd_insert_description
        )

        val taskDescription = expectedProject.description ?: ""


        composeTestRule.onNodeWithContentDescription(descriptionContentDescription)
            .assertTextEquals(taskDescription)


        // Test Date Selector
        val dateFormat = targetContext.getCurrentLocale()
            .getShortStyleFormatter()

        val startDateText = expectedProject.startDate?.format(dateFormat)
        val endDateText = expectedProject.endDate?.format(dateFormat)

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


    }
}