package com.teocoding.tasktrackr.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.utils.getCurrentLocale
import com.teocoding.tasktrackr.presentation.utils.getShortStyleFormatter
import java.time.LocalDate

@Composable
fun DeadlineRow(
    startDate: LocalDate?,
    endDate: LocalDate?,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CompositionLocalProvider(
            LocalContentColor
                    provides
                    MaterialTheme.colorScheme.onSurface
        ) {

            DateContainer(date = startDate)
        }


        DeadLineArrow(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
        )

        CompositionLocalProvider(
            LocalContentColor
                    provides
                    MaterialTheme.colorScheme.primary
        ) {

            DateContainer(date = endDate)

        }

    }

}


@Composable
private fun DateContainer(
    date: LocalDate?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val locale = context.getCurrentLocale()

    val dateFormatter = remember {

        locale.getShortStyleFormatter()
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_24dp),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = date?.let {
                dateFormatter.format(it)
            } ?: stringResource(id = R.string.no_date),
            style = MaterialTheme.typography.bodySmall
        )
    }

}

@Composable
fun DeadLineArrow(
    modifier: Modifier = Modifier
) {

    val arrowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)

    Canvas(
        modifier = modifier
    ) {

        val circleRadius = 6.dp.toPx()
        val headArrowWidth = circleRadius * 2
        val headArrowHeight = circleRadius * 2

        // Draw Circle at the start of the arrow
        drawCircle(
            color = arrowColor,
            radius = circleRadius,
            center = Offset(
                x = circleRadius,
                y = center.y
            )
        )


        // Draw the dashes line from the circle to the arrowhead
        val rectangleWidth = 5.dp.toPx()
        val rectangleSpaces = 5.dp.toPx()

        val xRectangles = (circleRadius * 2) + 2.dp.toPx()

        drawLine(
            color = arrowColor,
            start = Offset(
                x = xRectangles,
                y = center.y
            ),
            end = Offset(
                x = size.width - headArrowWidth,
                y = center.y
            ),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(
                    rectangleWidth,
                    rectangleSpaces
                ),
                phase = 0f
            )
        )

        val rectArrow = Rect(
            offset = Offset(
                x = size.width - headArrowWidth,
                y = center.y - (headArrowHeight / 2f)
            ),
            size = Size(
                width = headArrowWidth,
                height = headArrowHeight
            )
        )


        drawArrowHead(
            rect = rectArrow,
            arrowColor = arrowColor
        )

    }
}


private fun DrawScope.drawArrowHead(
    rect: Rect,
    arrowColor: Color
) {

    val topCornerOffset = Offset(
        x = rect.left,
        y = rect.top
    )

    val bottomCornerOffset = Offset(
        x = rect.left,
        y = rect.bottom
    )

    val endCornerOffset = Offset(
        x = rect.right,
        y = rect.center.y
    )


    val trianglePath = Path().apply {

        moveTo(
            x = topCornerOffset.x,
            y = topCornerOffset.y
        )

        lineTo(
            x = endCornerOffset.x,
            y = endCornerOffset.y
        )

        lineTo(
            x = bottomCornerOffset.x,
            y = bottomCornerOffset.y
        )

        close()

    }

    drawIntoCanvas { canvas ->

        canvas.drawOutline(
            outline = Outline.Generic(trianglePath),
            paint = Paint().apply {
                color = arrowColor
                pathEffect = PathEffect.cornerPathEffect(2.dp.toPx())
            }
        )

    }
}