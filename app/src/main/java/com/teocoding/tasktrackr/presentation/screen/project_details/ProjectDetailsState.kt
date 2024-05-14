package com.teocoding.tasktrackr.presentation.screen.project_details

import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.model.Task

@Immutable
data class ProjectDetailsState(
    val project: Project? = null,
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)
