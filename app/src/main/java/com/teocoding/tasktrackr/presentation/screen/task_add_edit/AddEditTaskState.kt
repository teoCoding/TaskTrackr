package com.teocoding.tasktrackr.presentation.screen.task_add_edit

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Project
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Immutable
data class AddEditTaskState(
    val projects: List<Project> = emptyList(),
    val selectedProject: Project? = null,
    val taskTitle: String = "",
    val taskDescription: String = "",
    val startDate: LocalDate? = LocalDate.now(),
    val endDate: LocalDate? = null,
    val priority: Priority = Priority.Low,
    val useEndDate: Boolean = false
) : Parcelable