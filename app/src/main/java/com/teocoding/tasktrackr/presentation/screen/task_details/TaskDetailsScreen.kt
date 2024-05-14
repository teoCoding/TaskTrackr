@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.teocoding.tasktrackr.presentation.screen.task_details

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.teocoding.tasktrackr.DummyData
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Photo
import com.teocoding.tasktrackr.presentation.components.MediumIconContainer
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.components.drop_down_menu.TaskTrackrDropDownMenu
import com.teocoding.tasktrackr.presentation.components.drop_down_menu.TaskTrackrDropDownMenuItem
import com.teocoding.tasktrackr.presentation.screen.photo.CameraPermissionHandler
import com.teocoding.tasktrackr.presentation.screen.photo.PermissionResult
import com.teocoding.tasktrackr.presentation.screen.photo.PhotoDialog
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import com.teocoding.tasktrackr.ui.navigation.other.ScreenRoute
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme
import java.time.LocalDate


@Composable
fun TaskDetailsScreen(
    goToScreen: (String) -> Unit,
    goBack: () -> Unit,
    onEvent: (TaskDetailsEvent) -> Unit,
    screenState: TaskDetailsState,
    contentPaddingValues: PaddingValues,
) {

    var photoToShow by remember {
        mutableStateOf<Photo?>(null)
    }

    var showDeleteTaskDialog by remember {
        mutableStateOf(false)
    }

    var showDeletePhotoDialog by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPaddingValues)
    ) {

        screenState.task?.let { task ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                TaskTrackrTopBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = goBack) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back_24dp),
                                contentDescription = null
                            )
                        }
                    },
                    actions = {

                        IconButton(
                            onClick = {
                                onEvent(TaskDetailsEvent.SwitchCompletedState)
                            }
                        ) {
                            if (task.isCompleted) {

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_uncheck_circle_24dp),
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check_circle_24dp),
                                    contentDescription = null
                                )
                            }
                        }

                        ScreenDropDownMenu(
                            onEditTaskClick = {
                                task.id?.let { id ->

                                    goToScreen(TaskRoute.AddEditTask.createRoute(id))
                                }
                            },
                            onDeleteTaskClick = {
                                showDeleteTaskDialog = true
                            }
                        )

                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(36.dp)
                ) {

                    TaskTitle(
                        projectTitle = screenState.taskProject?.title ?: "",
                        taskTitle = task.title,
                        priority = task.priority
                    )

                    DueDates(
                        startDate = task.startDate,
                        endDate = task.endDate,
                        modifier = Modifier.fillMaxWidth()
                    )

                    task.description?.let { description ->

                        Description(
                            description = description,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Images(
                        images = screenState.photos,
                        onClickTakePhoto = {
                            goToScreen(ScreenRoute.PhotoTaker.route)
                        },
                        onClickShowPhoto = { photo ->
                            photoToShow = photo
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

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

        photoToShow?.let { photo ->
            PhotoDialog(
                photo = photo,
                onDismissRequest = { photoToShow = null },
                onClickDelete = { showDeletePhotoDialog = true }
            )
        }

        if (showDeleteTaskDialog) {

            AlertDialog(
                onDismissRequest = { showDeleteTaskDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(TaskDetailsEvent.DeleteTask)
                            goBack()
                        }
                    ) {

                        Text(text = stringResource(id = R.string.yes))

                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteTaskDialog = false
                        }
                    ) {

                        Text(text = stringResource(id = R.string.no))

                    }
                },
                text = {
                    Text(text = stringResource(R.string.delete_task_message))
                }

            )
        }

        if (showDeletePhotoDialog) {

            AlertDialog(
                onDismissRequest = { showDeleteTaskDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val photo = photoToShow!!
                            photoToShow = null
                            showDeletePhotoDialog = false
                            onEvent(TaskDetailsEvent.DeletePhoto(photo))

                        }
                    ) {

                        Text(text = stringResource(id = R.string.yes))

                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeletePhotoDialog = false
                        }
                    ) {

                        Text(text = stringResource(id = R.string.no))

                    }
                },
                text = {
                    Text(text = stringResource(R.string.delete_photo_message))
                }

            )
        }

    }


}

@Composable
private fun ScreenDropDownMenu(
    onEditTaskClick: () -> Unit,
    onDeleteTaskClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        modifier = modifier,
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
    ) {

        TaskTrackrDropDownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.edit_task),
                )
            },
            onClick = {
                isMenuExpanded = false
                onEditTaskClick()
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
                    text = stringResource(R.string.delete_task),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                isMenuExpanded = false
                onDeleteTaskClick()
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
private fun TaskTitle(
    projectTitle: String,
    taskTitle: String,
    priority: Priority,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = taskTitle,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))


            val iconNumber by remember(priority) {

                val value = when (priority) {
                    Priority.Low -> 1
                    Priority.Medium -> 2
                    Priority.High -> 3
                }

                mutableIntStateOf(value)
            }

            repeat(iconNumber) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_priority_24dp),
                    contentDescription = null,
                    tint = when (priority) {
                        Priority.Low -> Color.Green
                        Priority.Medium -> Color.Blue
                        Priority.High -> Color.Red
                    }
                )
            }

        }

        CompositionLocalProvider(value = LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {

            Text(
                text = stringResource(R.string.project_caption_args, projectTitle),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }

}

@Composable
private fun DueDates(
    startDate: LocalDate?,
    endDate: LocalDate?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val locale = context.getCurrentLocale()

    val dateFormat = remember {
        locale.getShortStyleFormatter()
    }

    Row(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {


            MediumIconContainer(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_24dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(id = R.string.start_date),
                    style = MaterialTheme.typography.labelLarge
                )

                val dateText by remember(key1 = startDate) {
                    val text = startDate?.let { dateFormat.format(it) } ?: "---"

                    mutableStateOf(text)
                }


                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        endDate?.let { date ->

            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MediumIconContainer(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = stringResource(id = R.string.end_date),
                        style = MaterialTheme.typography.labelLarge,
                    )

                    val dateText by remember(key1 = date) {
                        val text = dateFormat.format(date)

                        mutableStateOf(text)
                    }


                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

    }

}

@Composable
private fun Description(
    description: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        SubtitleRow(
            icon = R.drawable.ic_description_24dp,
            text = stringResource(id = R.string.description)
        )

        CompositionLocalProvider(
            LocalContentColor provides
                    MaterialTheme.colorScheme.onSurfaceVariant
        ) {

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun Images(
    onClickTakePhoto: () -> Unit,
    onClickShowPhoto: (Photo) -> Unit,
    images: List<Photo>,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val activity = context as Activity

    val cameraPermissionHandler = remember {
        CameraPermissionHandler()
    }
    var showRationaleDialog by remember {
        mutableStateOf(false)
    }

    var showNeedPermissionDialog by remember {
        mutableStateOf(false)
    }


    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMap ->

        val canTakePhoto = permissionMap.all { map ->
            map.value
        }

        if (canTakePhoto) {
            onClickTakePhoto()
        } else {
            showNeedPermissionDialog = true
        }
    }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        SubtitleRow(
            icon = R.drawable.ic_images_24dp,
            text = stringResource(R.string.images)
        )

        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val containerModifier = Modifier
                .fillMaxWidth(0.4f)
                .aspectRatio(1.7f)

            images.forEach { photo ->

                ImageContainer(
                    modifier = containerModifier
                        .clickable {
                            onClickShowPhoto(photo)
                        }
                ) {

                    AsyncImage(
                        model = photo.localPath,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                }

            }

            ImagePlus(
                modifier = containerModifier
                    .clickable {

                        val permissionResult =
                            cameraPermissionHandler.checkPermissions(context, activity)

                        when (permissionResult) {
                            PermissionResult.Denied -> cameraPermissionLauncher.launch(
                                cameraPermissionHandler.permissionToRequest
                            )

                            PermissionResult.Granted -> onClickTakePhoto()

                            PermissionResult.ShowRequestPermissionRationale ->
                                showRationaleDialog = true
                        }

                    }
            )
        }
    }

    if (showRationaleDialog) {

        RequestPermissionRationaleDialog(
            onClickOk = {
                cameraPermissionLauncher.launch(cameraPermissionHandler.permissionToRequest)

                showRationaleDialog = false
            },
            onClickCancel = {
                showRationaleDialog = false
            },
            onDismissRequest = {
                showRationaleDialog = false
            }
        )

    }

    if (showNeedPermissionDialog) {

        NeedPermissionDialog(
            onDismissRequest = {
                showNeedPermissionDialog = false
            }
        )
    }

}

@Composable
private fun ImageContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {

    val borderColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .drawWithCache {

                onDrawBehind {
                    val stroke = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                        )
                    )

                    drawRoundRect(
                        color = borderColor,
                        cornerRadius = CornerRadius(
                            x = 16.dp.toPx(),
                            y = 16.dp.toPx()
                        ),
                        style = stroke,
                    )
                }
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun ImagePlus(
    modifier: Modifier = Modifier
) {

    ImageContainer(
        modifier = modifier
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_plus_24dp),
            contentDescription = null
        )
    }
}


@Composable
private fun RequestPermissionRationaleDialog(
    onClickOk: () -> Unit,
    onClickCancel: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(id = R.string.required_permission))
        },
        text = {
            Text(text = stringResource(R.string.camera_request_rationale))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onClickOk
            ) {
                Text(text = stringResource(id = R.string.ok))
            }

        },
        dismissButton = {
            TextButton(
                onClick = onClickCancel
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }

        }
    )

}

@Composable
private fun NeedPermissionDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.required_permission))
        },
        text = {
            Text(text = stringResource(R.string.explain_camera_permission))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(id = R.string.ok))
            }

        }
    )

}

@Composable
private fun SubtitleRow(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }

}


@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun TaskDetailsScreenPreview() {

    TaskTrackrTheme {

        TaskDetailsScreen(
            contentPaddingValues = PaddingValues(0.dp),
            goToScreen = {},
            goBack = {},
            onEvent = {},
            screenState = TaskDetailsState(
                task = DummyData.tasks.first(),
            )
        )
    }
}
