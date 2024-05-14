package com.teocoding.tasktrackr.presentation.screen.task_details

import com.teocoding.tasktrackr.domain.model.Photo

sealed interface TaskDetailsEvent {

    data object SwitchCompletedState : TaskDetailsEvent
    data object DeleteTask : TaskDetailsEvent
    data class DeletePhoto(val photo: Photo) : TaskDetailsEvent
}