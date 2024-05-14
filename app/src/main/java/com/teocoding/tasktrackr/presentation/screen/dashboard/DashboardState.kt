package com.teocoding.tasktrackr.presentation.screen.dashboard

import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.model.ProjectWithStats
import com.teocoding.tasktrackr.domain.model.Task

@Immutable
data class DashboardState(
    val projects: List<ProjectWithStats> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)
