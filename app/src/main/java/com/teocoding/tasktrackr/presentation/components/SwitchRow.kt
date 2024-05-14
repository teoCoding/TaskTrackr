package com.teocoding.tasktrackr.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SwitchRow(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .requiredHeight(48.dp)
            .toggleable(
                value = value,
                role = Role.Switch,
                onValueChange = onValueChange
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.labelLarge
            ) {

                text()

            }
        }

        TaskTrackrSwitch(
            checked = value,
            onCheckedChange = null,
        )

    }
}

