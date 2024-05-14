package com.teocoding.tasktrackr.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import com.teocoding.tasktrackr.domain.use_case.GetProjectsWithStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    taskRepository: TaskRepository,
    getProjectsWithStats: GetProjectsWithStats
) : ViewModel() {

    private val _screenState = MutableStateFlow(DashboardState())
    val screenState: StateFlow<DashboardState> = _screenState.asStateFlow()

    private val projects = getProjectsWithStats.execute()
        .onStart {
            _screenState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
        }

    private val tasks = taskRepository
        .getExpiringTasks()
        .onStart {
            _screenState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
        }


    init {
        projects.combine(tasks) { projects, expiringTasks ->

            _screenState.update { state ->
                state.copy(
                    projects = projects,
                    tasks = expiringTasks,
                    isLoading = false
                )

            }
        }
            .launchIn(viewModelScope)
    }

}