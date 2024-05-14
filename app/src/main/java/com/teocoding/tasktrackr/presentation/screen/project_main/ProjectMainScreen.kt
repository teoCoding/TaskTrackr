package com.teocoding.tasktrackr.presentation.screen.project_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.components.FilterTabRow
import com.teocoding.tasktrackr.presentation.components.ProjectCard
import com.teocoding.tasktrackr.presentation.components.SmallFabCreate
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.components.TopBarHeight
import com.teocoding.tasktrackr.presentation.components.search_text_field.SearchTextField
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme

@Composable
fun ProjectsMainScreen(
    contentPaddingValues: PaddingValues,
    goToScreen: (String) -> Unit,
    goBack: () -> Unit,
    onEvent: (ProjectMainEvent) -> Unit,
    screenState: ProjectMainState
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(contentPaddingValues)
    ) {

        TaskTrackrTopBar(
            title = {
                Text(text = stringResource(id = R.string.projects))

                Icon(
                    painter = painterResource(id = R.drawable.ic_project_24dp),
                    contentDescription = null
                )
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

        Column(
            modifier = Modifier
                .offset(y = TopBarHeight)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {


            SearchTextField(
                value = screenState.searchText,
                onValueChange = { text ->
                    onEvent(ProjectMainEvent.OnSearchTextChange(text))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            FilterTabRow(
                selectedFilter = screenState.filter,
                onTabClick = { filter ->
                    onEvent(ProjectMainEvent.OnFilterChange(filter))
                },
                modifier = Modifier
                    .fillMaxWidth()
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        bottom = 150.dp
                    )
                ) {

                    items(
                        items = screenState.projects,
                        key = { item ->
                            item.project.id!!
                        }
                    ) { project ->

                        ProjectCard(
                            project = project,
                            onClick = {
                                goToScreen(ProjectRoute.Details.createRoute(project.project.id!!))
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

@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ProjectMainScreenPreview() {

    TaskTrackrTheme {
        ProjectsMainScreen(
            contentPaddingValues = PaddingValues(0.dp),
            goToScreen = {},
            goBack = {},
            onEvent = {},
            screenState = ProjectMainState(
                projects = DummyData.projectsWithStats,
            )
        )
    }
}

