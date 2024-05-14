package com.teocoding.tasktrackr.domain

import com.teocoding.tasktrackr.domain.model.Project
import javax.inject.Inject

class InsertTaskValidator @Inject constructor() {

    fun execute(
        project: Project?,
        taskTitle: String,
    ): TaskValidatorResult {

        if (project == null) {
            return TaskValidatorResult.NullProject
        }

        if (taskTitle.isBlank()) {
            return TaskValidatorResult.BlankTitle
        }

        return TaskValidatorResult.Success

    }
}


sealed interface TaskValidatorResult {

    data object NullProject : TaskValidatorResult

    data object BlankTitle : TaskValidatorResult

    data object Success : TaskValidatorResult
}