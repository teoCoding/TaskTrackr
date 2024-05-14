package com.teocoding.tasktrackr.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.model.ProjectWithStats
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getIntegerFormat
import com.teocoding.tasktrackr.presentation.utils.getPercentFormat
import kotlin.math.absoluteValue

@Composable
fun ProjectCard(
    project: ProjectWithStats,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = project.project.title,
                style = MaterialTheme.typography.titleMedium
            )

            DeadlineRow(
                startDate = project.project.startDate,
                endDate = project.project.endDate,
                modifier = Modifier
                    .fillMaxWidth()
            )

            ProgressRow(
                progressPercent = project.stats.percentTasksCompleted,
                completedTasks = project.stats.completedTasks,
                totalTasks = project.stats.totalTasks,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}


@Composable
private fun ProgressRow(
    progressPercent: Float,
    completedTasks: Int,
    totalTasks: Int,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val locale = context.getCurrentLocale()

    val percentFormat = remember {
        locale.getPercentFormat()
    }

    val integerFormat = remember {
        locale.getIntegerFormat()
    }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {

        Text(
            text = percentFormat.format(progressPercent),
            style = MaterialTheme.typography.bodySmall
        )

        ProjectProgressBar(
            progress = progressPercent,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 12.dp)
        )


        val taskString = buildAnnotatedString {

            withStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.primary)
            ) {
                append(integerFormat.format(completedTasks))
            }

            append("/")

            val tasks = pluralStringResource(
                id = R.plurals.tasks,
                count = totalTasks.absoluteValue,
                totalTasks
            )

            append(tasks)

        }


        Text(text = taskString, style = MaterialTheme.typography.bodySmall)

    }
}


@Composable
private fun ProjectProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {

    val colorBackgroundLine = MaterialTheme.colorScheme.secondaryContainer
    val colorCompletedLine = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {

        val lineHeight = 8.dp.toPx()
        val capWidth = lineHeight / 2f


        // Draw the line in background

        val startLineOffSet = Offset(
            x = capWidth,
            y = center.y
        )

        val endLineOffSet = Offset(
            x = size.width - capWidth,
            y = center.y
        )


        drawLine(
            color = colorBackgroundLine,
            start = startLineOffSet,
            end = endLineOffSet,
            strokeWidth = lineHeight,
            cap = StrokeCap.Round
        )


        // Draw the progress line

        if (progress == 0f) {
            return@Canvas
        }

        val progressLineLength = (size.width * progress) - capWidth

        val completedLineEnd = Offset(
            x = progressLineLength,
            y = center.y
        )

        drawLine(
            color = colorCompletedLine,
            start = startLineOffSet,
            end = completedLineEnd,
            strokeWidth = lineHeight,
            cap = StrokeCap.Round
        )

    }

}