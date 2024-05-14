package com.teocoding.tasktrackr.presentation.screen.project_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val projectRepository: ProjectRepository,
    taskRepository: TaskRepository
) : ViewModel() {


    private val _screenState = MutableStateFlow(ProjectDetailsState())
    val screenState: StateFlow<ProjectDetailsState> = _screenState.asStateFlow()

    private val projectId =
        checkNotNull(
            savedStateHandle.get<Long>(ProjectRoute.Details.PROJECT_ID)
        )

    init {

        projectRepository.getProjectById(projectId)
            .onStart {
                _screenState.update { state ->
                    state.copy(
                        isLoading = true
                    )
                }
            }
            .combine(taskRepository.getTaskByProject(projectId)) { project, tasks ->

                _screenState.update { state ->
                    state.copy(
                        isLoading = false,
                        project = project,
                        tasks = tasks
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ProjectDetailsEvent) {

        when (event) {

            ProjectDetailsEvent.DeleteProject -> {
                deleteProject()
            }

            is ProjectDetailsEvent.SwitchCompletedState -> {
                updateCompletedProject(event.isCompleted)
            }
        }
    }

    private fun updateCompletedProject(isCompleted: Boolean) {

        viewModelScope.launch {

            projectRepository.updateCompleted(
                projectId = projectId,
                isCompleted = isCompleted
            )
        }
    }

    private fun deleteProject() {

        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
        }
    }


}


