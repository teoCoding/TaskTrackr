@file:OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class
)

package com.teocoding.tasktrackr.presentation.screen.project_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.presentation.components.DeadlineRow
import com.teocoding.tasktrackr.presentation.components.MediumIconContainer
import com.teocoding.tasktrackr.presentation.components.SmallFabCreate
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.components.TopBarHeight
import com.teocoding.tasktrackr.presentation.components.drop_down_menu.TaskTrackrDropDownMenu
import com.teocoding.tasktrackr.presentation.components.drop_down_menu.TaskTrackrDropDownMenuItem
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme

@Composable
fun ProjectDetailsScreen(
    contentPaddingValues: PaddingValues,
    goBack: () -> Unit,
    goToScreen: (String) -> Unit,
    onEvent: (ProjectDetailsEvent) -> Unit,
    screenState: ProjectDetailsState
) {

    var showDeleteProjectDialog by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(contentPaddingValues),
    ) {

        TaskTrackrTopBar(
            title = {
                Text(text = stringResource(id = R.string.project_details))

            },
            navigationIcon = {
                IconButton(onClick = { goBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back_24dp),
                        contentDescription = null
                    )
                }
            },
            actions = {
                SmallFabCreate(
                    clickOnCreateProject = { goToScreen(ProjectRoute.AddEditProject.route) },
                    clickOnCreateTask = { goToScreen(TaskRoute.AddEditTask.route) }
                )
            }
        )

        screenState.project?.let { project ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = TopBarHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                TitleRow(
                    project = project,
                    onClickSwitchCompleted = {
                        onEvent(
                            ProjectDetailsEvent.SwitchCompletedState(
                                isCompleted = !project.isCompleted
                            )
                        )
                    },
                    onClickEditProject = {
                        screenState.project.id?.let { id ->

                            goToScreen(ProjectRoute.AddEditProject.createRoute(id))
                        }
                    },
                    onClickDeleteProject = {
                        showDeleteProjectDialog = true
                    }
                )


                project.description?.let { description ->

                    DescriptionSection(
                        description = description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    text = stringResource(id = R.string.tasks),
                    style = MaterialTheme.typography.titleMedium
                )


                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        bottom = 150.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = screenState.tasks,
                        key = {
                            it.id!!
                        }
                    ) { task ->

                        TaskCard(
                            task = task,
                            onClick = { goToScreen(TaskRoute.Details.createRoute(task.id!!)) },
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                    }
                }

            }
        }

        if (screenState.isLoading) {

            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                strokeCap = StrokeCap.Round
            )
        }

        if (showDeleteProjectDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteProjectDialog = false

                },
                confirmButton = {

                    TextButton(
                        onClick = {
                            onEvent(ProjectDetailsEvent.DeleteProject)
                            showDeleteProjectDialog = false
                            goBack()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.yes))
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {
                            showDeleteProjectDialog = false
                        }
                    ) {
                        Text(text = stringResource(id = R.string.no))
                    }
                },
                text = {
                    Text(text = stringResource(R.string.delete_project_message))
                }
            )
        }

    }

}


@Composable
private fun TitleRow(
    project: Project,
    onClickSwitchCompleted: () -> Unit,
    onClickEditProject: () -> Unit,
    onClickDeleteProject: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {


        MediumIconContainer(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_project_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(20.dp)
            )

        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = project.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        LocalDropDownMenu(
            onClickSwitchCompleted = onClickSwitchCompleted,
            onClickEditProject = onClickEditProject,
            onClickDeleteProject = onClickDeleteProject,
            isCompleted = project.isCompleted,
        )

    }
}

@Composable
private fun LocalDropDownMenu(
    onClickSwitchCompleted: () -> Unit,
    onClickEditProject: () -> Unit,
    onClickDeleteProject: () -> Unit,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {

    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    var menuOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    val density = LocalDensity.current

    IconButton(
        modifier = modifier
            .onGloballyPositioned {
                val position = it.positionInParent()
                with(density) {
                    val offsetDp = DpOffset(
                        x = position.x.toDp(),
                        y = position.y.toDp()
                    )
                    menuOffset = offsetDp
                }

            },
        onClick = { isMenuExpanded = !isMenuExpanded }
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_more_24dp),
            contentDescription = null
        )
    }


    TaskTrackrDropDownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { isMenuExpanded = false },
        offset = menuOffset
    ) {

        TaskTrackrDropDownMenuItem(
            text = {
                Text(
                    text = if (isCompleted) {
                        stringResource(R.string.mark_not_completed)
                    } else {
                        stringResource(R.string.mark_completed)
                    }

                )
            },
            onClick = {
                onClickSwitchCompleted()
                isMenuExpanded = false
            },
            leadingIcon = {
                Icon(
                    painter = if (isCompleted) {
                        painterResource(id = R.drawable.ic_uncheck_circle_24dp)
                    } else {
                        painterResource(id = R.drawable.ic_check_circle_24dp)
                    },
                    contentDescription = null
                )
            }
        )

        TaskTrackrDropDownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.edit_project),
                )
            },
            onClick = {
                onClickEditProject()
                isMenuExpanded = false
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_edit_24dp),
                    contentDescription = null
                )
            }
        )

        TaskTrackrDropDownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.delete_project),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                onClickDeleteProject()
                isMenuExpanded = false
            },
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_delete_24dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )

    }

}

@Composable
private fun DescriptionSection(
    description: String,
    modifier: Modifier = Modifier
) {

    var isDescriptionExpanded by remember {
        mutableStateOf(false)
    }

    val transition = updateTransition(
        targetState = isDescriptionExpanded,
        label = "Expand Transition"
    )

    val arrowRotation by transition.animateFloat(
        label = "Arrow Animation"
    ) { isExpanded ->
        if (isExpanded) {
            180f
        } else {
            0f
        }
    }


    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .clickable {
                    isDescriptionExpanded = !isDescriptionExpanded
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down_24dp),
                contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    rotationZ = arrowRotation
                }
            )
        }

        transition.AnimatedVisibility(
            visible = { isDescriptionExpanded },
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {

            CompositionLocalProvider(
                LocalContentColor provides
                        MaterialTheme.colorScheme.onSurfaceVariant
            ) {

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_task_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            DeadlineRow(
                startDate = task.startDate,
                endDate = task.endDate,
                modifier = Modifier
                    .fillMaxWidth()
            )


        }

    }

}


@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ProjectDetailsScreenPreview() {

    TaskTrackrTheme {
        ProjectDetailsScreen(
            contentPaddingValues = PaddingValues(0.dp),
            goToScreen = {},
            goBack = {},
            onEvent = {},
            screenState = ProjectDetailsState(
                project = DummyData.projects.first(),
                tasks = DummyData.tasks
            )
        )
    }
}