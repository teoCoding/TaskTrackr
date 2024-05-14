package com.teocoding.tasktrackr.ui.navigation.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.teocoding.tasktrackr.presentation.screen.dashboard.DashboardScreen
import com.teocoding.tasktrackr.presentation.screen.dashboard.DashboardViewModel
import com.teocoding.tasktrackr.presentation.screen.search.SearchScreen
import com.teocoding.tasktrackr.presentation.screen.search.SearchViewModel

fun NavGraphBuilder.dashboardGraph(
    navController: NavController,
    contentPadding: PaddingValues
) {

    navigation(startDestination = DashboardRoute.Main.route, route = DashboardRoute.Root.route) {

        composable(
            route = DashboardRoute.Main.route
        ) {

            val viewModel = hiltViewModel<DashboardViewModel>()

            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            DashboardScreen(
                contentPaddingValues = contentPadding,
                screenState = screenState,
                goToScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(
            route = DashboardRoute.Search.route
        ) {
            val viewModel = hiltViewModel<SearchViewModel>()

            val screenState by viewModel.screenState.collectAsStateWithLifecycle()

            SearchScreen(
                contentPaddingValues = contentPadding,
                goToScreen = navController::navigate,
                onSearchTextChange = viewModel::onSearchTextChange,
                screenState = screenState,
            )

        }
    }

}
