@file:OptIn(ExperimentalLayoutApi::class)

package com.teocoding.tasktrackr.presentation.screen.task_add_edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.presentation.components.DateSelector
import com.teocoding.tasktrackr.presentation.components.TaskTrackrButton
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTextField
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.utils.asString
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme

@Composable
fun AddEditTaskScreen(
    contentPaddingValues: PaddingValues,
    onGoBack: () -> Unit,
    onEvent: (AddTaskEvent) -> Unit,
    screenState: AddEditTaskState
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPaddingValues)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {

            TaskTrackrTopBar(
                title = {
                    Text(text = stringResource(id = R.string.save_task))
                },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back_24dp),
                            contentDescription = null
                        )
                    }
                },
                actions = null,
            )


            ProjectSelector(
                projects = screenState.projects,
                selectedProject = screenState.selectedProject,
                onClickProject = { onEvent(AddTaskEvent.OnSelectedProjectChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(16.dp))

            val titleContentDescription = stringResource(id = R.string.cd_insert_title)

            TaskTrackrTextField(
                value = screenState.taskTitle,
                onValueChange = {
                    onEvent(AddTaskEvent.OnTitleChange(it))
                },
                caption = {
                    Text(text = stringResource(R.string.title))
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = titleContentDescription }
            )


            Spacer(modifier = Modifier.height(16.dp))

            val descriptionContentDescription = stringResource(id = R.string.cd_insert_description)

            TaskTrackrTextField(
                value = screenState.taskDescription,
                onValueChange = {
                    onEvent(AddTaskEvent.OnDescriptionChange(it))
                },
                caption = {
                    Text(text = stringResource(R.string.description))
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = descriptionContentDescription }
            )

            Spacer(modifier = Modifier.height(24.dp))

            DateSelector(
                startDate = screenState.startDate,
                onStartDateSelected = {
                    onEvent(AddTaskEvent.OnStartDateChange(it))
                },
                endDate = screenState.endDate,
                onEndDateSelected = {
                    onEvent(AddTaskEvent.OnEndDateChange(it))
                },
                useEndDate = screenState.useEndDate,
                onUseEndDateChanged = {
                    onEvent(AddTaskEvent.OnUseEndDateChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Priority(
                currentPriority = screenState.priority,
                onPriorityChange = {
                    onEvent(AddTaskEvent.OnPriorityChange(it))
                },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1f))

            TaskTrackrButton(
                onClick = {
                    onEvent(AddTaskEvent.OnSaveTask)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.save_task))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
                    contentDescription = null
                )
            }

        }
    }

}


@Composable
private fun ProjectSelector(
    projects: List<Project>,
    selectedProject: Project?,
    onClickProject: (Project) -> Unit,
    modifier: Modifier = Modifier
) {

    var showProjects by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .clickable {
                    showProjects = !showProjects
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if (selectedProject != null) {

                Text(
                    text = stringResource(R.string.project_caption),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )

                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                    Text(
                        text = selectedProject.title,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(end = 4.dp)
                    )


                }
            } else {

                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                    Text(
                        text = stringResource(R.string.choose_project),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down_24dp),
                contentDescription = null
            )

        }

        AnimatedVisibility(visible = showProjects) {

            if (projects.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(text = "No project", style = MaterialTheme.typography.labelMedium)
                }

            } else {

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 2
                ) {

                    projects.forEach { project ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .defaultMinSize(minHeight = 48.dp)
                                .weight(1f)
                                .clickable {
                                    onClickProject(project)
                                    showProjects = false
                                }
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.ic_project_24dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = project.title,
                                style = MaterialTheme.typography.bodyLarge

                            )
                        }
                    }

                }
            }

        }

    }

}

@Composable
private fun Priority(
    currentPriority: Priority,
    onPriorityChange: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {

        Text(
            text = stringResource(id = R.string.priority),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Priority.entries.forEach { priority ->

                FilterChip(
                    selected = currentPriority == priority,
                    onClick = { onPriorityChange(priority) },
                    label = {
                        Text(text = priority.asString())
                    }
                )
            }

        }

    }

}

@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun AddEditTaskScreenPreview() {

    TaskTrackrTheme {

        AddEditTaskScreen(
            contentPaddingValues = PaddingValues(0.dp),
            onEvent = {},
            onGoBack = {},
            screenState = AddEditTaskState(
                projects = DummyData.projects,
            )
        )
    }
}
