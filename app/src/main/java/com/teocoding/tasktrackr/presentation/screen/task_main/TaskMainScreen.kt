package com.teocoding.tasktrackr.presentation.screen.task_main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.presentation.components.FilterTabRow
import com.teocoding.tasktrackr.presentation.components.SmallFabCreate
import com.teocoding.tasktrackr.presentation.components.SmallIconContainer
import com.teocoding.tasktrackr.presentation.components.TaskTrackrCheckBox
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.components.TopBarHeight
import com.teocoding.tasktrackr.presentation.components.search_text_field.SearchTextField
import com.teocoding.tasktrackr.presentation.utils.asString
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme
import java.time.LocalDate

@Composable
fun TaskMainScreen(
    goToScreen: (String) -> Unit,
    goBack: () -> Unit,
    contentPaddingValues: PaddingValues,
    onEvent: (TaskMainEvent) -> Unit,
    screenState: TaskMainState,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPaddingValues)

    ) {

        TaskTrackrTopBar(
            title = {
                Text(text = stringResource(id = R.string.tasks))

                Icon(
                    painter = painterResource(id = R.drawable.ic_task_24dp),
                    contentDescription = null
                )
            },
            navigationIcon = {
                IconButton(onClick = goBack) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = TopBarHeight)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            SearchTextField(
                value = screenState.searchText,
                onValueChange = {
                    onEvent(TaskMainEvent.OnSearchTextChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            FilterTabRow(
                selectedFilter = screenState.filter,
                onTabClick = {
                    onEvent(TaskMainEvent.OnFilterChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (screenState.tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {

                    TextButton(onClick = { goToScreen(TaskRoute.AddEditTask.route) }) {

                        Text(text = stringResource(R.string.create_your_first_task))

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
                            contentDescription = null
                        )
                    }
                }

            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 150.dp)
                ) {

                    items(
                        items = screenState.tasks,
                        key = {
                            it.id!!
                        }
                    ) { task ->
                        TaskCard(
                            task = task,
                            updateCompleted = { id, completed ->
                                onEvent(
                                    TaskMainEvent.OnUpdateCompleted(
                                        id = id,
                                        isCompleted = completed
                                    )
                                )
                            },
                            onClick = {
                                goToScreen(TaskRoute.Details.createRoute(task.id!!))
                            },
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
    }

}

@Composable
private fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    updateCompleted: (taskId: Long, completed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            TaskTitle(
                title = task.title,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TaskDeadline(
                endDate = task.endDate,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            TaskPriority(
                priority = task.priority,
                modifier = Modifier
                    .fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))


            CheckRow(
                isChecked = task.isCompleted,
                onCheckedChange = { checked ->
                    updateCompleted(task.id!!, checked)
                },
                isCheckedText = stringResource(R.string.done),
                isNotCheckedText = stringResource(R.string.mark_as_done),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}

@Composable
private fun TaskTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        SmallIconContainer(containerColor = MaterialTheme.colorScheme.primaryContainer) {
            Icon(
                painter = painterResource(id = R.drawable.ic_task_24dp),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun TaskDeadline(
    endDate: LocalDate?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val locale = context.getCurrentLocale()

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time_24dp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.deadline),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val dateFormat = remember {
            locale.getShortStyleFormatter()
        }

        val endDateText by remember(endDate) {
            val text = endDate?.let { date ->
                dateFormat.format(date)
            } ?: "-----"

            mutableStateOf(text)
        }

        Text(
            text = endDateText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .offset(
                    x = 24.dp
                )
        )

    }
}

@Composable
private fun TaskPriority(
    priority: Priority,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CompositionLocalProvider(
                LocalContentColor provides
                        MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_priority_24dp),
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.priority),
                    style = MaterialTheme.typography.labelMedium
                )

            }

        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = priority.asString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .offset(
                    x = 24.dp
                )
        )
    }
}


@Composable
private fun CheckRow(
    isChecked: Boolean,
    isCheckedText: String,
    isNotCheckedText: String,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val textStyle = MaterialTheme.typography.labelMedium

    Row(
        modifier = modifier
            .toggleable(
                value = isChecked,
                onValueChange = onCheckedChange,
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        TaskTrackrCheckBox(isChecked = isChecked, onCheckedChange = null)

        AnimatedContent(
            targetState = isChecked,
            label = "CheckRow animated text",
            transitionSpec = {
                (
                        fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                scaleIn(
                                    initialScale = 0.92f,
                                    transformOrigin = TransformOrigin(
                                        pivotFractionX = 0.0f,
                                        pivotFractionY = 0.5f
                                    ),
                                    animationSpec = tween(220, delayMillis = 90)
                                )
                        )
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            }
        ) { checked ->

            if (checked) {

                Text(text = isCheckedText, style = textStyle)

            } else {

                Text(text = isNotCheckedText, style = textStyle)

            }

        }
    }

}


@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun TaskMainScreenPreview() {

    TaskTrackrTheme {

        TaskMainScreen(
            contentPaddingValues = PaddingValues(0.dp),
            goToScreen = {},
            goBack = {},
            onEvent = {},
            screenState = TaskMainState(
                tasks = DummyData.tasks
            )
        )
    }
}