package com.teocoding.tasktrackr.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTrackrDatePickerDialog(
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    shape: Shape = DatePickerDefaults.shape,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    tonalElevation: Dp = DatePickerDefaults.TonalElevation
) {

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        colors = colors,
        shape = shape,
        properties = properties,
        tonalElevation = tonalElevation
    ) {

        DatePicker(
            state = state,
            colors = colors
        )

    }

}