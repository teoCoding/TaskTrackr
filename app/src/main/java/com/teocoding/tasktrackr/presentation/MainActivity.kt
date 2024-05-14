package com.teocoding.tasktrackr.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.presentation.screen.MainViewModel
import com.teocoding.tasktrackr.presentation.screen.photo.PhotoTackerScreen
import com.teocoding.tasktrackr.presentation.screen.photo.PhotoUtils
import com.teocoding.tasktrackr.presentation.screen.project_add_edit.AddEditProjectScreen
import com.teocoding.tasktrackr.presentation.screen.project_add_edit.AddEditProjectViewModel
import com.teocoding.tasktrackr.presentation.screen.settings.SettingsScreen
import com.teocoding.tasktrackr.presentation.screen.settings.SettingsViewModel
import com.teocoding.tasktrackr.presentation.screen.task_add_edit.AddEditTaskScreen
import com.teocoding.tasktrackr.presentation.screen.task_add_edit.AddEditTaskViewModel
import com.teocoding.tasktrackr.ui.navigation.dashboard.DashboardRoute
import com.teocoding.tasktrackr.ui.navigation.dashboard.dashboardGraph
import com.teocoding.tasktrackr.ui.navigation.other.ScreenRoute
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import com.teocoding.tasktrackr.ui.navigation.project.projectGraph
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import com.teocoding.tasktrackr.ui.navigation.task.taskGraph
import com.teocoding.tasktrackr.ui.theme.TaskTrackrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {

            val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()

            TaskTrackrTheme(
                darkTheme = when (darkMode) {
                    AppCompatDelegate.MODE_NIGHT_NO -> false
                    AppCompatDelegate.MODE_NIGHT_YES -> true
                    else -> isSystemInDarkTheme()
                }
            ) {

                val navController = rememberNavController()
                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState
                            )
                        },
                        bottomBar = {

                            val currentRoute =
                                navController.currentBackStackEntryAsState().value?.destination?.route

                            val routeWithOutBottomBar = remember {
                                listOf(
                                    ProjectRoute.AddEditProject.route,
                                    TaskRoute.AddEditTask.route
                                )
                            }

                            val isBottomBarVisible by remember(key1 = currentRoute) {
                                derivedStateOf {
                                    !routeWithOutBottomBar.contains(currentRoute)
                                }
                            }

                            AnimatedVisibility(
                                visible = isBottomBarVisible,
                                enter = scaleIn(),
                                exit = scaleOut()
                            ) {

                                BottomNavigation(
                                    navController = navController,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }


                        }
                    ) { contentPadding ->

                        NavHost(
                            navController = navController,
                            startDestination = DashboardRoute.Root.route
                        ) {

                            dashboardGraph(
                                navController = navController,
                                contentPadding = contentPadding
                            )

                            projectGraph(
                                navController = navController,
                                contentPadding = contentPadding
                            )

                            taskGraph(
                                navController = navController,
                                contentPadding = contentPadding
                            )

                            composable(
                                route = ScreenRoute.PhotoTaker.route
                            ) {
                                PhotoTackerScreen(
                                    onPhotoTaken = { photoUri ->
                                        navController.popBackStack()
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            key = PhotoUtils.PHOTO_TAKEN_URI_ARG,
                                            value = photoUri
                                        )
                                    },
                                    contentPaddingValues = contentPadding
                                )
                            }

                            composable(
                                route = TaskRoute.AddEditTask.route,
                                arguments = listOf(
                                    navArgument(
                                        name = TaskRoute.AddEditTask.TASK_ID
                                    ) {
                                        type = NavType.LongType
                                        defaultValue = TaskRoute.AddEditTask.DEFAULT_VALUE_TASK_ID
                                    }
                                )
                            ) {

                                val viewModel = hiltViewModel<AddEditTaskViewModel>()

                                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                                val error by viewModel.error.collectAsStateWithLifecycle()

                                val context = LocalContext.current

                                LaunchedEffect(key1 = error) {
                                    error?.let { resId ->
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(resId)
                                        )

                                        viewModel.onErrorShown()
                                    }
                                }

                                val goBack by viewModel.goBack.collectAsStateWithLifecycle()

                                LaunchedEffect(key1 = goBack) {
                                    if (goBack) {
                                        navController.popBackStack()
                                        viewModel.onGoneBack()
                                    }
                                }


                                AddEditTaskScreen(
                                    contentPaddingValues = contentPadding,
                                    onGoBack = navController::popBackStack,
                                    screenState = screenState,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            composable(
                                route = ProjectRoute.AddEditProject.route,
                                arguments = listOf(
                                    navArgument(
                                        name = ProjectRoute.AddEditProject.PROJECT_ID
                                    ) {
                                        type = NavType.LongType
                                        defaultValue =
                                            ProjectRoute.AddEditProject.DEFAULT_VALUE_PROJECT_ID
                                    }
                                )
                            ) {

                                val viewModel = hiltViewModel<AddEditProjectViewModel>()

                                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                                val error by viewModel.error.collectAsStateWithLifecycle()

                                val context = LocalContext.current

                                LaunchedEffect(key1 = error) {
                                    error?.let { resId ->
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(resId)
                                        )

                                        viewModel.onErrorShown()
                                    }
                                }

                                val goBack by viewModel.goBack.collectAsStateWithLifecycle()

                                LaunchedEffect(key1 = goBack) {
                                    if (goBack) {
                                        navController.popBackStack()
                                        viewModel.onGoneBack()
                                    }
                                }


                                AddEditProjectScreen(
                                    contentPaddingValues = contentPadding,
                                    onGoBack = navController::popBackStack,
                                    screenState = screenState,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            composable(
                                route = ScreenRoute.Settings.route
                            ) {

                                val viewModel = hiltViewModel<SettingsViewModel>()

                                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                                SettingsScreen(
                                    contentPaddingValues = contentPadding,
                                    onEvent = viewModel::onEvent,
                                    screenState = screenState
                                )

                            }

                        }

                    }
                }
            }
        }
    }

}


enum class NavigationBarDestinations(
    @DrawableRes val icon: Int,
    val route: String
) {
    Dashboard(
        icon = R.drawable.ic_home_24dp,
        route = DashboardRoute.Root.route
    ),

    Projects(
        icon = R.drawable.ic_project_24dp,
        route = ProjectRoute.Root.route
    ),

    Tasks(
        icon = R.drawable.ic_task_24dp,
        route = TaskRoute.Root.route
    ),

    Settings(
        icon = R.drawable.ic_settings_24dp,
        route = ScreenRoute.Settings.route
    )
}

@Composable
private fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    Box(
        modifier =
        modifier
            .padding(24.dp)
    ) {


        Surface(
            modifier = Modifier
                .height(64.dp),
            shape = MaterialTheme.shapes.large,
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                NavigationBarDestinations.entries.forEachIndexed { index, item ->

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    val isSelected by remember(key1 = currentDestination) {
                        derivedStateOf {
                            currentDestination?.hierarchy?.any { it.route == item.route } == true
                        }
                    }


                    TaskTrackrNavigationItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                tint = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    )

                }

            }

        }
    }

}

@Composable
private fun TaskTrackrNavigationItem(
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    Column(
        modifier = modifier
            .fillMaxHeight(0.75f)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(50))
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        icon()

        if (selected) {

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier =
                Modifier
                    .size(6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50)
                    )
            )
        }

    }

}