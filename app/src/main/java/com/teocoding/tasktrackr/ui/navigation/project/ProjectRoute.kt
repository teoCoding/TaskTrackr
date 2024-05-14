package com.teocoding.tasktrackr.ui.navigation.project

import com.teocoding.tasktrackr.ui.navigation.NavigationConst

sealed class ProjectRoute(val route: String) {

    data object Root : ProjectRoute("project")

    data object Main : ProjectRoute("project_main")

    data object Details : ProjectRoute("project_details/{projectId}") {

        const val PROJECT_ID = "projectId"

        const val DEEP_LINK =
            "${NavigationConst.INTERNAL_BASE_DEEP_LINK}/project_details/projectId={projectId}"

        fun createDeepLink(projectId: Long): String {
            return "app://tasktrackr.com/project_details/projectId=$projectId"
        }

        fun createRoute(projectId: Long): String {
            return "project_details/$projectId"
        }

    }

    data object AddEditProject : ProjectRoute("add_edit_project/?projectId={projectId}") {

        const val PROJECT_ID = "projectId"
        const val DEFAULT_VALUE_PROJECT_ID = -1L

        fun createRoute(projectId: Long): String {
            return "add_edit_project/?projectId=$projectId"
        }

    }

}