package com.teocoding.tasktrackr.ui.navigation.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.teocoding.tasktrackr.presentation.screen.project_details.ProjectDetailsScreen
import com.teocoding.tasktrackr.presentation.screen.project_details.ProjectDetailsViewModel
import com.teocoding.tasktrackr.presentation.screen.project_main.ProjectMainViewModel
import com.teocoding.tasktrackr.presentation.screen.project_main.ProjectsMainScreen

fun NavGraphBuilder.projectGraph(
    navController: NavController,
    contentPadding: PaddingValues
) {

    navigation(startDestination = ProjectRoute.Main.route, route = ProjectRoute.Root.route) {

        composable(
            route = ProjectRoute.Main.route
        ) {

            val viewModel = hiltViewModel<ProjectMainViewModel>()

            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            ProjectsMainScreen(
                contentPaddingValues = contentPadding,
                screenState = screenState,
                onEvent = viewModel::onEvent,
                goToScreen = navController::navigate,
                goBack = navController::popBackStack
            )
        }

        composable(
            route = ProjectRoute.Details.route,
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = ProjectRoute.Details.DEEP_LINK
                }
            ),
            arguments = listOf(
                navArgument(
                    name = ProjectRoute.Details.PROJECT_ID
                ) {
                    this.type = NavType.LongType
                }
            )
        ) {

            val viewModel = hiltViewModel<ProjectDetailsViewModel>()

            val screenState by viewModel.screenState.collectAsStateWithLifecycle()


            ProjectDetailsScreen(
                screenState = screenState,
                goBack = navController::popBackStack,
                goToScreen = navController::navigate,
                onEvent = viewModel::onEvent,
                contentPaddingValues = contentPadding
            )
        }
    }

}
