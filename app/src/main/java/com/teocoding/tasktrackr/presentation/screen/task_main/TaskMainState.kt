package com.teocoding.tasktrackr.presentation.screen.task_main

import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.filter.Filter
import com.teocoding.tasktrackr.domain.model.Task

@Immutable
data class TaskMainState(
    val tasks: List<Task> = emptyList(),
    val filter: Filter = Filter.All,
    val isLoading: Boolean = false,
    val searchText: String = ""
)
