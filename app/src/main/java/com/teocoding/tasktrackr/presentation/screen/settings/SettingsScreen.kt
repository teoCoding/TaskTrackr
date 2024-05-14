@file:OptIn(ExperimentalMaterial3Api::class)

package com.teocoding.tasktrackr.presentation.screen.settings

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.components.SwitchRow
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTimePickerDialog
import com.teocoding.tasktrackr.presentation.components.TaskTrackrTopBar
import com.teocoding.tasktrackr.presentation.components.TopBarHeight
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.time.format.FormatStyle


@Composable
fun SettingsScreen(
    contentPaddingValues: PaddingValues,
    onEvent: (SettingsEvent) -> Unit,
    screenState: SettingsState
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
                Text(text = "Settings")

                Icon(
                    painter = painterResource(id = R.drawable.ic_settings_24dp),
                    contentDescription = null
                )
            },
            navigationIcon = null,
            actions = null
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = TopBarHeight)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp)
        ) {


            Notifications(
                areNotificationsDeadlineEnabled = screenState.deadLineNotificationEnabled,
                onNotificationsDeadlineEnabledChange = { enabled ->
                    onEvent(SettingsEvent.SwitchDeadlineNotificationEnabled(enabled))
                },
                deadlineNotificationsTime = screenState.deadlineNotificationTime,
                onNotificationsDeadlineTimeChange = { hour, minute ->
                    onEvent(SettingsEvent.DeadlineNotificationTimeChanged(hour, minute))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            DarkThemeSelector(
                selectedDarkMode = screenState.darkMode,
                onDarkModeChanged = {
                    onEvent(SettingsEvent.DarkModeChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }


    }

}


@Composable
private fun Notifications(
    areNotificationsDeadlineEnabled: Boolean,
    onNotificationsDeadlineEnabledChange: (Boolean) -> Unit,
    deadlineNotificationsTime: LocalTime,
    onNotificationsDeadlineTimeChange: (hour: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val locale = context.getCurrentLocale()

    var areNotificationEnabled by remember {
        val areEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

        } else {
            true
        }

        mutableStateOf(areEnabled)
    }


    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {

        Title(
            icon = R.drawable.ic_notifications_24dp,
            text = stringResource(R.string.notifications),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (areNotificationEnabled) {

            SwitchRow(
                value = areNotificationsDeadlineEnabled,
                onValueChange = onNotificationsDeadlineEnabledChange,
                text = {
                    Text(
                        text = stringResource(R.string.notify_deadline)
                    )
                },
                modifier = Modifier
                    .padding(start = 32.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = areNotificationsDeadlineEnabled
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(48.dp)
                        .clickable {
                            showTimePickerDialog = true
                        }
                        .padding(start = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(R.string.notification_time),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    val formatter = remember {
                        DateTimeFormatter.ofLocalizedTime(
                            FormatStyle.SHORT
                        )
                            .withLocale(locale)
                            .withDecimalStyle(DecimalStyle.of(locale))
                    }

                    Text(
                        text = formatter.format(deadlineNotificationsTime),
                        style = MaterialTheme.typography.bodyMedium
                    )

                }

            }

        } else {

            RequestNotificationsPermissionRow(
                areNotificationsEnabled = { areEnabled ->
                    areNotificationEnabled = areEnabled
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

        }


    }

    if (showTimePickerDialog) {

        val timePickerState = rememberTimePickerState(
            initialHour = deadlineNotificationsTime.hour,
            initialMinute = deadlineNotificationsTime.minute,
        )

        TaskTrackrTimePickerDialog(
            state = timePickerState,
            onDismissRequest = { showTimePickerDialog = false },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onNotificationsDeadlineTimeChange(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    showTimePickerDialog = false
                }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        )

    }

}


@Composable
private fun Title(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun RequestNotificationsPermissionRow(
    areNotificationsEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val activity = context as Activity

    var showRequestRationaleDialog by remember {
        mutableStateOf(false)
    }

    var showNeedNotificationPermissionDialog by remember {
        mutableStateOf(false)
    }

    val postNotificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isEnabled ->

        if (isEnabled) {
            areNotificationsEnabled(isEnabled)
        } else {

            showNeedNotificationPermissionDialog = true

        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(48.dp)
            .clickable {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                ) {
                    showRequestRationaleDialog = true
                } else {

                    postNotificationPermissionLauncher
                        .launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(R.string.grant_permission_to_receive_notifications),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward_24dp),
            contentDescription = null
        )

    }

    if (showRequestRationaleDialog) {

        AlertDialog(
            onDismissRequest = { showRequestRationaleDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        postNotificationPermissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )

                        showRequestRationaleDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRequestRationaleDialog = false }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            text = {
                Text(text = stringResource(R.string.notification_permission_rationale_message))
            }
        )
    }

    if (showNeedNotificationPermissionDialog) {

        AlertDialog(
            onDismissRequest = { showNeedNotificationPermissionDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNeedNotificationPermissionDialog = false
                    }
                )
                {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            text = {
                Text(text = stringResource(R.string.notification_permission_rationale_message))
            }
        )
    }
}


@Composable
private fun DarkThemeSelector(
    selectedDarkMode: Int,
    onDarkModeChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {

        Title(icon = R.drawable.ic_night_mode_24dp, text = stringResource(R.string.dark_theme))

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {

            RadioButtonRow(
                text = stringResource(R.string.light_mode),
                selected = selectedDarkMode == AppCompatDelegate.MODE_NIGHT_NO,
                onClick = { onDarkModeChanged(AppCompatDelegate.MODE_NIGHT_NO) },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(48.dp)
                    .padding(start = 32.dp)
            )

            RadioButtonRow(
                text = stringResource(R.string.dark_mode),
                selected = selectedDarkMode == AppCompatDelegate.MODE_NIGHT_YES,
                onClick = { onDarkModeChanged(AppCompatDelegate.MODE_NIGHT_YES) },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(48.dp)
                    .padding(start = 32.dp)
            )

            RadioButtonRow(
                text = stringResource(R.string.system_default),
                selected = selectedDarkMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                onClick = { onDarkModeChanged(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) },
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(48.dp)
                    .padding(start = 32.dp)
            )

        }

    }
}


@Composable
private fun RadioButtonRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selected,
            onClick = null
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

}













