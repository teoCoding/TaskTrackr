package com.teocoding.tasktrackr.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.Priority

@Composable
fun Priority.asString(): String {

    return when (this) {
        Priority.Low -> stringResource(id = R.string.low)
        Priority.Medium -> stringResource(id = R.string.medium)
        Priority.High -> stringResource(id = R.string.high)
    }
}