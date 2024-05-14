package com.teocoding.tasktrackr.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import com.teocoding.tasktrackr.presentation.utils.toUtcLocalDate
import com.teocoding.tasktrackr.presentation.utils.toUtcTimeMillis
import java.time.Instant
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    startDate: LocalDate?,
    onStartDateSelected: (LocalDate?) -> Unit,
    endDate: LocalDate?,
    onEndDateSelected: (LocalDate?) -> Unit,
    useEndDate: Boolean,
    onUseEndDateChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDatePickerStartDate by remember {
        mutableStateOf(false)
    }

    var showDatePickerEndDate by remember {
        mutableStateOf(false)
    }

    val startDateDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate?.toUtcTimeMillis(),
    )

    val endDateDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = endDate?.toUtcTimeMillis()
    )


    Column(modifier = modifier) {

        Text(
            text = stringResource(R.string.calendar),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            DateContainer(
                selectedDate = startDate,
                color = MaterialTheme.colorScheme.primaryContainer,
                caption = stringResource(R.string.start_date),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        showDatePickerStartDate = true
                    }
            )

            AnimatedVisibility(
                modifier = Modifier.weight(1f),
                visible = useEndDate,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {

                DateContainer(
                    selectedDate = endDate,
                    color = MaterialTheme.colorScheme.primary,
                    caption = stringResource(R.string.end_date),
                    modifier = Modifier
                        .clickable {
                            showDatePickerEndDate = true
                        }
                )

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        SwitchRow(
            value = useEndDate,
            onValueChange = onUseEndDateChanged,
            text = {
                Text(
                    text = stringResource(R.string.end_date)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }

    if (showDatePickerStartDate) {

        TaskTrackrDatePickerDialog(
            state = startDateDatePickerState,
            onDismissRequest = { showDatePickerStartDate = false },
            dismissButton = {
                OutlinedButton(onClick = {

                    showDatePickerStartDate = false
                }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            confirmButton = {
                Button(onClick = {
                    val selectedDateInMillis =
                        startDateDatePickerState.selectedDateMillis

                    val selectedDate = selectedDateInMillis?.let { millis ->
                        Instant.ofEpochMilli(millis).toUtcLocalDate()
                    }

                    onStartDateSelected(selectedDate)

                    showDatePickerStartDate = false
                }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        )
    }

    if (showDatePickerEndDate) {
        TaskTrackrDatePickerDialog(
            state = endDateDatePickerState,
            onDismissRequest = { showDatePickerEndDate = false },
            dismissButton = {
                OutlinedButton(onClick = {

                    showDatePickerEndDate = false
                }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            confirmButton = {
                Button(onClick = {
                    val selectedDateInMillis =
                        endDateDatePickerState.selectedDateMillis

                    val selectedDate = selectedDateInMillis?.let { millis ->
                        Instant.ofEpochMilli(millis).toUtcLocalDate()
                    }

                    onEndDateSelected(selectedDate)

                    showDatePickerEndDate = false
                }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        )
    }

}

@Composable
private fun DateContainer(
    selectedDate: LocalDate?,
    color: Color,
    caption: String,
    modifier: Modifier = Modifier,
) {


    val context = LocalContext.current
    val locale = context.getCurrentLocale()

    val dateFormat = remember {
        locale.getShortStyleFormatter()
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MediumIconContainer(containerColor = color) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar_24dp),
                contentDescription = null,
                tint = contentColorFor(color)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {

            Text(
                text = caption,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = selectedDate?.let { date ->
                    dateFormat.format(date)
                } ?: stringResource(id = R.string.select_date),
                style = MaterialTheme.typography.bodyMedium
            )
        }


    }
}