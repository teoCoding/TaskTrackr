package com.teocoding.tasktrackr.ui.navigation.task

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
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
import com.teocoding.tasktrackr.presentation.screen.photo.PhotoUtils
import com.teocoding.tasktrackr.presentation.screen.task_details.TaskDetailsScreen
import com.teocoding.tasktrackr.presentation.screen.task_details.TaskDetailsViewModel
import com.teocoding.tasktrackr.presentation.screen.task_main.TaskMainScreen
import com.teocoding.tasktrackr.presentation.screen.task_main.TaskMainViewModel

fun NavGraphBuilder.taskGraph(
    navController: NavController,
    contentPadding: PaddingValues
) {

    navigation(startDestination = TaskRoute.Main.route, route = TaskRoute.Root.route) {

        composable(
            route = TaskRoute.Main.route
        ) {

            val viewModel = hiltViewModel<TaskMainViewModel>()

            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            TaskMainScreen(
                contentPaddingValues = contentPadding,
                goToScreen = navController::navigate,
                goBack = navController::popBackStack,
                onEvent = viewModel::onEvent,
                screenState = screenState
            )
        }

        composable(
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = TaskRoute.Details.DEEP_LINK
                }
            ),
            route = TaskRoute.Details.route,
            arguments = listOf(
                navArgument(
                    name = TaskRoute.Details.TASK_ID
                ) {
                    type = NavType.LongType
                }
            )
        ) { navBackStackEntry ->

            val viewModel = hiltViewModel<TaskDetailsViewModel>()

            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            val photoUri by navBackStackEntry.savedStateHandle
                .getStateFlow<Uri?>(PhotoUtils.PHOTO_TAKEN_URI_ARG, null)
                .collectAsStateWithLifecycle()


            LaunchedEffect(key1 = photoUri) {

                photoUri?.let { uri ->
                    viewModel.savePhoto(uri)

                    navBackStackEntry.savedStateHandle.remove<Uri>(PhotoUtils.PHOTO_TAKEN_URI_ARG)

                }

            }

            TaskDetailsScreen(
                screenState = screenState,
                goToScreen = navController::navigate,
                goBack = navController::popBackStack,
                onEvent = viewModel::onEvent,
                contentPaddingValues = contentPadding
            )

        }
    }

}
