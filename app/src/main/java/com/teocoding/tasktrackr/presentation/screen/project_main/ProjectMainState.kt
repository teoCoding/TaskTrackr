package com.teocoding.tasktrackr.presentation.screen.project_main

import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.filter.Filter
import com.teocoding.tasktrackr.domain.model.ProjectWithStats

@Immutable
data class ProjectMainState(
    val projects: List<ProjectWithStats> = emptyList(),
    val filter: Filter = Filter.All,
    val searchText: String = "",
    val isLoading: Boolean = false
)
