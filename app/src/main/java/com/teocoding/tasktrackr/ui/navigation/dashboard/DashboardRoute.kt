package com.teocoding.tasktrackr.ui.navigation.dashboard

sealed class DashboardRoute(val route: String) {

    data object Root : DashboardRoute("dashboard_root")

    data object Main : DashboardRoute("dashboard")

    data object Search : DashboardRoute("search")
}