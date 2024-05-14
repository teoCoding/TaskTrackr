package com.teocoding.tasktrackr.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.R

@Composable
fun TaskTrackrCheckBox(
    isChecked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier
) {

    val checkableModifier = if (onCheckedChange != null) {
        modifier
            .toggleable(
                value = isChecked,
                role = Role.Checkbox,
                onValueChange = onCheckedChange
            )
    } else {
        modifier
    }

    Box(
        modifier = checkableModifier
            .size(24.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
    ) {

        AnimatedVisibility(
            visible = isChecked,
            label = "CheckBoxAnimation",
            enter = scaleIn(
                animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )

            ) + fadeIn(),
            exit = scaleOut(
                animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeOut(),
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_check_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }
    }

}