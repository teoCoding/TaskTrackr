package com.teocoding.tasktrackr.presentation.screen.task_details

import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.model.Photo
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.model.Task

@Immutable
data class TaskDetailsState(
    val task: Task? = null,
    val photos: List<Photo> = emptyList(),
    val taskProject: Project? = null,
    val isLoading: Boolean = false
)
