package com.teocoding.tasktrackr.domain

import javax.inject.Inject

class InsertProjectValidator @Inject constructor() {

    fun execute(
        projectTitle: String,
    ): ProjectValidatorResult {


        if (projectTitle.isBlank()) {

            return ProjectValidatorResult.BlankTitle
        }

        return ProjectValidatorResult.Success

    }
}


sealed interface ProjectValidatorResult {

    data object BlankTitle : ProjectValidatorResult

    data object Success : ProjectValidatorResult
}