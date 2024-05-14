package com.teocoding.tasktrackr.presentation.screen.project_add_edit

import java.time.LocalDate

sealed interface AddProjectEvent {
    data class OnTitleChange(val text: String) : AddProjectEvent
    data class OnDescriptionChange(val text: String) : AddProjectEvent
    data class OnStartDateChange(val date: LocalDate?) : AddProjectEvent
    data class OnEndDateChange(val date: LocalDate?) : AddProjectEvent
    data class OnUseEndDateChange(val useEndDate: Boolean) : AddProjectEvent
    data object OnSaveProject : AddProjectEvent
}