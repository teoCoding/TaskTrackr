package com.teocoding.tasktrackr.presentation.components.tab

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TaskTrackrTabRow(
    selectedTabIndex: Int,
    selectedTabText: String,
    modifier: Modifier = Modifier,
    containerColor: Color = TabRowDefaults.primaryContainerColor,
    contentColor: Color = TabRowDefaults.primaryContentColor,
    edgePadding: Dp = 16.dp,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->

        TabIndicator(
            tabPosition = tabPositions[selectedTabIndex],
            selectedTabText = selectedTabText
        )
    },
    divider: @Composable () -> Unit = {},
    tabs: @Composable () -> Unit
) {

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        edgePadding = edgePadding,
        indicator = indicator,
        divider = divider,
        tabs = tabs
    )

}


@Composable
private fun TabIndicator(
    tabPosition: TabPosition,
    selectedTabText: String,
    modifier: Modifier = Modifier,

    ) {

    Box(
        modifier = modifier
            .tabIndicatorOffset(tabPosition)
            .defaultMinSize(minHeight = 40.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center

    ) {

        AnimatedContent(
            targetState = selectedTabText,
            label = "Text Tab Indicator"
        ) { text ->

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
}