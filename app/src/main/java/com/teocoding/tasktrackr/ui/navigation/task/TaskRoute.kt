package com.teocoding.tasktrackr.ui.navigation.task

import com.teocoding.tasktrackr.ui.navigation.NavigationConst

sealed class TaskRoute(val route: String) {

    data object Root : TaskRoute("task")

    data object Main : TaskRoute("task_main")

    data object Details : TaskRoute("task_details/{taskId}") {

        const val TASK_ID = "taskId"

        const val DEEP_LINK =
            "${NavigationConst.INTERNAL_BASE_DEEP_LINK}/task_details/taskId={taskId}"

        fun createDeepLink(taskId: Long): String {
            return "app://tasktrackr.com/task_details/taskId=$taskId"
        }

        fun createRoute(taskId: Long): String {
            return "task_details/$taskId"
        }
    }

    data object AddEditTask : TaskRoute("add_edit_task/?taskId={taskId}") {

        const val TASK_ID = "taskId"
        const val DEFAULT_VALUE_TASK_ID = -1L

        fun createRoute(taskId: Long): String {
            return "add_edit_task/?taskId=$taskId"
        }
    }

}