package com.teocoding.tasktrackr.ui.navigation.other

sealed class ScreenRoute(val route: String) {

    data object PhotoTaker : ScreenRoute("photo_taker")

    data object Settings : ScreenRoute("settings")
}