package com.teocoding.tasktrackr.presentation.screen.task_main

import com.teocoding.tasktrackr.domain.filter.Filter

sealed interface TaskMainEvent {

    data class OnSearchTextChange(val text: String) : TaskMainEvent

    data class OnFilterChange(val filter: Filter) : TaskMainEvent

    data class OnUpdateCompleted(val id: Long, val isCompleted: Boolean) : TaskMainEvent
}