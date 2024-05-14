package com.teocoding.tasktrackr.presentation.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.presentation.components.LargeIconContainer
import com.teocoding.tasktrackr.presentation.components.search_text_field.SearchTextField
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme

@Composable
fun SearchScreen(
    contentPaddingValues: PaddingValues,
    goToScreen: (String) -> Unit,
    onSearchTextChange: (String) -> Unit,
    screenState: SearchScreenState
) {


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPaddingValues),
        color = MaterialTheme.colorScheme.background

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            val textFieldFocusRequester = remember {
                FocusRequester()
            }

            val keyBoardController = LocalSoftwareKeyboardController.current

            SearchTextField(
                value = screenState.searchText,
                onValueChange = onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester = textFieldFocusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )

            LaunchedEffect(key1 = true) {
                textFieldFocusRequester.requestFocus()
                keyBoardController?.show()
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(
                    items = screenState.queryResult,
                ) { item ->
                    when (item) {
                        is Project -> {

                            SearchProjectCard(
                                onClick = {
                                    goToScreen(ProjectRoute.Details.createRoute(item.id!!))
                                },
                                projectName = item.title
                            )
                        }

                        is Task -> {
                            SearchTaskCard(
                                onClick = {
                                    goToScreen(TaskRoute.Details.createRoute(item.id!!))
                                },
                                taskName = item.title
                            )
                        }

                        else -> throw IllegalArgumentException(
                            "Please provide an implementation for class: ${item::class.java.name}"
                        )
                    }
                }

            }

        }

    }

}

@Composable
private fun SearchProjectCard(
    onClick: () -> Unit,
    projectName: String,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LargeIconContainer(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_project_24dp),
                    contentDescription = null
                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
                ) {

                    Text(
                        text = stringResource(id = R.string.project),
                        style = MaterialTheme.typography.labelSmall
                    )
                }


                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = projectName,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }


    }
}

@Composable
private fun SearchTaskCard(
    onClick: () -> Unit,
    taskName: String,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            LargeIconContainer(
                containerColor = MaterialTheme.colorScheme.primaryContainer
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
            ) {

                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
                ) {

                    Text(
                        text = stringResource(id = R.string.task),
                        style = MaterialTheme.typography.labelSmall
                    )
                }


                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = taskName,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }


    }
}

@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun SearchScreenPreview() {

    TaskTrackrTheme {
        SearchScreen(
            contentPaddingValues = PaddingValues(0.dp),
            goToScreen = {},
            onSearchTextChange = {},
            screenState = SearchScreenState(
                queryResult =
                (DummyData.projects.take(3) + DummyData.tasks.take(3))
                    .shuffled()
            )
        )
    }
}