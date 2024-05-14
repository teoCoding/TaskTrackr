package com.teocoding.tasktrackr.presentation.screen.project_add_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.components.DateSelector
import com.teocoding.tasktrackr.presentation.components.TaskTrackrButton
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTextField
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme

@Composable
fun AddEditProjectScreen(
    contentPaddingValues: PaddingValues,
    onGoBack: () -> Unit,
    screenState: AddProjectState,
    onEvent: (AddProjectEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPaddingValues)
    ) {

        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                )
                .navigationBarsPadding()
        ) {

            TaskTrackrTopBar(
                title = {
                    Text(text = stringResource(id = R.string.save_project))
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

            val titleContentDescription = stringResource(id = R.string.cd_insert_title)

            TaskTrackrTextField(
                value = screenState.projectTitle,
                onValueChange = { onEvent(AddProjectEvent.OnTitleChange(it)) },
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
                value = screenState.projectDescription,
                onValueChange = { onEvent(AddProjectEvent.OnDescriptionChange(it)) },
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
                    onEvent(AddProjectEvent.OnStartDateChange(it))
                },
                endDate = screenState.endDate,
                onEndDateSelected = {
                    onEvent(AddProjectEvent.OnEndDateChange(it))
                },
                useEndDate = screenState.useEndDate,
                onUseEndDateChanged = {
                    onEvent(AddProjectEvent.OnUseEndDateChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            TaskTrackrButton(
                onClick = {
                    onEvent(AddProjectEvent.OnSaveProject)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.save_project))

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
                    contentDescription = null
                )
            }

        }
    }


}

@Preview
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun AddEditProjectScreenPreview() {

    TaskTrackrTheme {

        AddEditProjectScreen(
            contentPaddingValues = PaddingValues(0.dp),
            onGoBack = {},
            onEvent = {},
            screenState = AddProjectState()
        )
    }
}