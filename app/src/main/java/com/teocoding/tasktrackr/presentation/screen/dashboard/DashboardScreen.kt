package com.teocoding.tasktrackr.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.model.ProjectWithStats
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.presentation.components.LargeIconContainer
import com.teocoding.tasktrackr.presentation.components.ProjectCard
import com.teocoding.tasktrackr.presentation.components.SmallFabCreate
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.components.TopBarHeight
import com.teocoding.tasktrackr.presentation.components.search_text_field.SearchTextField
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import com.teocoding.tasktrackr.ui.navigation.dashboard.DashboardRoute
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme

@Composable
fun DashboardScreen(
    contentPaddingValues: PaddingValues,
    goToScreen: (String) -> Unit,
    screenState: DashboardState,
) {

    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(contentPaddingValues)
    ) {

        TaskTrackrTopBar(

            title = {
                Text(text = stringResource(R.string.dashboard))

                Icon(
                    painter = painterResource(id = R.drawable.ic_dashboard_24dp),
                    contentDescription = null
                )
            },
            navigationIcon = null,
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            val interactionSourceSearch = remember {
                MutableInteractionSource()
            }

            val isTextFieldPressed by interactionSourceSearch.collectIsPressedAsState()

            LaunchedEffect(key1 = isTextFieldPressed) {
                if (isTextFieldPressed) {
                    goToScreen(DashboardRoute.Search.route)
                }
            }

            SearchTextField(
                value = "", onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true,
                interactionSource = interactionSourceSearch
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProjectTitle(
                onClick = { goToScreen(ProjectRoute.Main.route) },
                modifier = Modifier.fillMaxWidth()
            )

            if (screenState.projects.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {

                    TextButton(onClick = { goToScreen(ProjectRoute.AddEditProject.route) }) {

                        Text(text = stringResource(R.string.create_your_first_project))

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
                            contentDescription = null
                        )
                    }
                }

            } else {

                ProjectsRow(
                    projects = screenState.projects,
                    onClickItem = { projectId ->
                        goToScreen(ProjectRoute.Details.createRoute(projectId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp
                        )
                )
            }

            TaskTitle(
                onClick = {
                    goToScreen(TaskRoute.Main.route)
                },
                modifier = Modifier.fillMaxWidth()
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

                TasksColumn(
                    tasks = screenState.tasks,
                    onClickItem = { taskId ->
                        goToScreen(TaskRoute.Details.createRoute(taskId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(
                            vertical = 16.dp
                        )
                )
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
private fun ProjectTitle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_project_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(id = R.string.projects),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
private fun ProjectsRow(
    projects: List<ProjectWithStats>,
    onClickItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = projects,
            key = { it.project.id!! }
        ) { project ->

            ProjectCard(
                project = project,
                onClick = { onClickItem(project.project.id!!) },
                modifier = Modifier
                    .fillParentMaxWidth()
            )
        }
    }
}


@Composable
private fun TaskTitle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_task_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(id = R.string.tasks),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
private fun TasksColumn(
    tasks: List<Task>,
    onClickItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 50.dp)
    ) {

        items(
            items = tasks,
            key = { it.id!! }
        ) { task ->

            TaskCard(
                task = task,
                onClickItem = onClickItem
            )

        }
    }

}

@Composable
private fun TaskCard(
    task: Task,
    onClickItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val locale = context.getCurrentLocale()

    ElevatedCard(
        onClick = { onClickItem(task.id!!) },
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LargeIconContainer(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_task_24dp),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                Text(text = task.title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )


                    Spacer(modifier = Modifier.width(8.dp))

                    val formatter = remember {
                        locale.getShortStyleFormatter()
                    }

                    val endDate by remember(task.endDate) {

                        val endDateText = task.endDate?.let {
                            formatter.format(it)
                        } ?: "---"

                        mutableStateOf(endDateText)
                    }


                    Text(
                        text = stringResource(
                            id = R.string.deadline_date,
                            endDate
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

            }
        }
    }
}

@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun DashBoardScreenPreview() {

    TaskTrackrTheme {

        DashboardScreen(
            contentPaddingValues = PaddingValues(0.dp),
            goToScreen = {},
            screenState = DashboardState(
                projects = DummyData.projectsWithStats,
                tasks = DummyData.tasks
            )
        )
    }
}