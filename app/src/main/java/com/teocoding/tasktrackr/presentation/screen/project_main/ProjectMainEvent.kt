package com.teocoding.tasktrackr.presentation.screen.project_main

import com.teocoding.tasktrackr.domain.filter.Filter

sealed interface ProjectMainEvent {

    data class OnSearchTextChange(val text: String) : ProjectMainEvent

    data class OnFilterChange(val filter: Filter) : ProjectMainEvent
}