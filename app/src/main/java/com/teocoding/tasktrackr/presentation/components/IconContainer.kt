package com.teocoding.tasktrackr.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


/**
 * A medium rounded container for the icon.
 * The size is 28.dp.
 * @param containerColor The color of the container
 * @param content the icon wrapped in a composable
 */
@Composable
fun SmallIconContainer(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable () -> Unit
) {

    IconContainer(
        containerColor = containerColor,
        modifier = modifier
            .size(28.dp),
        shape = MaterialTheme.shapes.small,
        content = content
    )

}


/**
 * A medium rounded container for the icon.
 * The size is 40.dp.
 * @param containerColor The color of the container
 * @param content the icon wrapped in a composable
 */
@Composable
fun MediumIconContainer(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable () -> Unit
) {

    IconContainer(
        containerColor = containerColor,
        modifier = modifier
            .size(40.dp),
        content = content
    )

}

/**
 * A large rounded container for the icon.
 * The size is 48.dp.
 * @param containerColor The color of the container
 * @param content the icon wrapped in a composable
 */

@Composable
fun LargeIconContainer(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable () -> Unit
) {

    IconContainer(
        containerColor = containerColor,
        modifier = modifier
            .size(48.dp),
        content = content
    )

}


@Composable
private fun IconContainer(
    containerColor: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
    content: @Composable () -> Unit
) {


    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {

        CompositionLocalProvider(value = LocalContentColor provides contentColorFor(containerColor)) {

            content()
        }

    }
}