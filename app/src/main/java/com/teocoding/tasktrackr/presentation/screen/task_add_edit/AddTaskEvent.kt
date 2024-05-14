package com.teocoding.tasktrackr.presentation.screen.task_add_edit

import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.model.Project
import java.time.LocalDate

sealed interface AddTaskEvent {

    data class OnSelectedProjectChange(val project: Project) : AddTaskEvent
    data class OnTitleChange(val text: String) : AddTaskEvent
    data class OnDescriptionChange(val text: String) : AddTaskEvent
    data class OnStartDateChange(val date: LocalDate?) : AddTaskEvent
    data class OnEndDateChange(val date: LocalDate?) : AddTaskEvent
    data class OnUseEndDateChange(val useEndDate: Boolean) : AddTaskEvent
    data class OnPriorityChange(val priority: Priority) : AddTaskEvent
    data object OnSaveTask : AddTaskEvent
}