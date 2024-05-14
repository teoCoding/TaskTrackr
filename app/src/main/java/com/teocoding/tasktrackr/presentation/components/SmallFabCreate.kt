package com.teocoding.tasktrackr.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.components.drop_down_menu.TaskTrackrDropDownMenu
import com.teocoding.tasktrackr.presentation.components.drop_down_menu.TaskTrackrDropDownMenuItem

@Composable
fun SmallFabCreate(
    clickOnCreateProject: () -> Unit,
    clickOnCreateTask: () -> Unit,
    modifier: Modifier = Modifier
) {

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {

    }

    SmallFloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = { showDropDownMenu = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_plus_24dp),
            contentDescription = null
        )
    }

    TaskTrackrDropDownMenu(
        expanded = showDropDownMenu,
        onDismissRequest = { showDropDownMenu = false }
    ) {

        TaskTrackrDropDownMenuItem(
            text = {
                Text(text = stringResource(R.string.create_project))
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_project_24dp),
                    contentDescription = null
                )
            },
            onClick = {
                clickOnCreateProject()
                showDropDownMenu = false
            }
        )

        TaskTrackrDropDownMenuItem(
            text = {
                Text(text = stringResource(R.string.create_task))
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_24dp),
                    contentDescription = null
                )
            },
            onClick = {
                clickOnCreateTask()
                showDropDownMenu = false
            }
        )

    }
}