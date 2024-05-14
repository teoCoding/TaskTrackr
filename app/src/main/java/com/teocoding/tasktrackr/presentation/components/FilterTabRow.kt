package com.teocoding.tasktrackr.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.filter.Filter
import com.teocoding.tasktrackr.presentation.components.tab.TaskTrackrTab
import com.teocoding.tasktrackr.presentation.components.tab.TaskTrackrTabRow

@Composable
fun FilterTabRow(
    selectedFilter: Filter,
    onTabClick: (Filter) -> Unit,
    modifier: Modifier = Modifier
) {


    TaskTrackrTabRow(
        modifier = modifier,
        selectedTabIndex = selectedFilter.ordinal,
        selectedTabText = getFilterText(filter = selectedFilter)
    ) {

        Filter.entries.forEach { filter ->

            TaskTrackrTab(
                selected = selectedFilter == filter,
                onClick = { onTabClick(filter) },
            ) {
                Text(
                    text = getFilterText(filter = filter)
                )
            }
        }

    }
}

@Composable
private fun getFilterText(
    filter: Filter
): String {
    return when (filter) {
        Filter.All -> stringResource(id = R.string.all)
        Filter.Completed -> stringResource(id = R.string.completed)
        Filter.NotCompleted -> stringResource(id = R.string.not_completed)
    }
}