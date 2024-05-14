package com.teocoding.tasktrackr.presentation.screen.project_details

sealed interface ProjectDetailsEvent {

    data class SwitchCompletedState(val isCompleted: Boolean) : ProjectDetailsEvent

    data object DeleteProject : ProjectDetailsEvent
}