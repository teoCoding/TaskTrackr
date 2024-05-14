package com.teocoding.tasktrackr.presentation.screen.project_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.InsertProjectValidator
import com.teocoding.tasktrackr.domain.ProjectValidatorResult
import com.teocoding.tasktrackr.domain.model.Project
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.presentation.utils.update
import com.teocoding.tasktrackr.ui.navigation.project.ProjectRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SCREEN_STATE_KEY = "add_project_screen_state"

@HiltViewModel
class AddEditProjectViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val projectRepository: ProjectRepository,
    private val insertProjectValidator: InsertProjectValidator
) : ViewModel() {

    val screenState = savedStateHandle.getStateFlow(SCREEN_STATE_KEY, AddProjectState())

    private val _error = MutableStateFlow<Int?>(null)
    val error: StateFlow<Int?> = _error.asStateFlow()

    private val _goBack = MutableStateFlow(false)
    val goBack: StateFlow<Boolean> = _goBack.asStateFlow()

    private val projectToEditId = savedStateHandle.get<Long>(ProjectRoute.AddEditProject.PROJECT_ID)
        .takeIf { id -> id != ProjectRoute.AddEditProject.DEFAULT_VALUE_PROJECT_ID }

    init {
        projectToEditId?.let { id ->

            projectRepository.getProjectById(id)
                .filterNotNull()
                .onEach { project ->

                    savedStateHandle.update<AddProjectState>(
                        key = SCREEN_STATE_KEY
                    ) { state ->
                        state?.copy(
                            projectTitle = project.title,
                            projectDescription = project.description ?: "",
                            startDate = project.startDate,
                            endDate = project.endDate,
                            useEndDate = project.endDate != null
                        )
                    }

                }
                .launchIn(viewModelScope)
        }
    }


    fun onEvent(event: AddProjectEvent) {
        when (event) {
            is AddProjectEvent.OnDescriptionChange -> {

                savedStateHandle.update<AddProjectState>(SCREEN_STATE_KEY) { state ->
                    state?.copy(
                        projectDescription = event.text
                    )
                }

            }

            is AddProjectEvent.OnEndDateChange -> {

                savedStateHandle.update<AddProjectState>(SCREEN_STATE_KEY) { state ->
                    state?.copy(
                        endDate = event.date
                    )
                }
            }

            is AddProjectEvent.OnStartDateChange -> {

                savedStateHandle.update<AddProjectState>(SCREEN_STATE_KEY) { state ->
                    state?.copy(
                        startDate = event.date
                    )
                }
            }

            is AddProjectEvent.OnTitleChange -> {

                savedStateHandle.update<AddProjectState>(SCREEN_STATE_KEY) { state ->
                    state?.copy(
                        projectTitle = event.text
                    )
                }
            }

            is AddProjectEvent.OnUseEndDateChange -> {

                savedStateHandle.update<AddProjectState>(SCREEN_STATE_KEY) { state ->
                    state?.copy(
                        useEndDate = event.useEndDate
                    )
                }
            }

            AddProjectEvent.OnSaveProject -> {

                if (projectToEditId == null) {

                    insertProject()
                } else {
                    updateProject()
                }
            }
        }
    }


    private fun insertProject() {

        viewModelScope.launch {

            val addProject = screenState.value

            val validationResult = insertProjectValidator.execute(
                projectTitle = addProject.projectTitle
            )

            when (validationResult) {
                ProjectValidatorResult.BlankTitle -> {

                    _error.update {
                        R.string.error_insert_title
                    }

                    return@launch
                }


                ProjectValidatorResult.Success -> Unit
            }


            val endDate = addProject.endDate.takeIf { screenState.value.useEndDate }

            val project = Project(
                id = null,
                title = addProject.projectTitle.trim(),
                description = addProject.projectDescription.trim().ifBlank { null },
                startDate = addProject.startDate,
                endDate = endDate,
                completedDate = null,
                isCompleted = false
            )

            projectRepository.insertProject(project)

            _goBack.update { true }

        }
    }

    private fun updateProject() {

        viewModelScope.launch {

            val addProject = screenState.value

            val validationResult = insertProjectValidator.execute(
                projectTitle = addProject.projectTitle
            )

            when (validationResult) {
                ProjectValidatorResult.BlankTitle -> {

                    _error.update {
                        R.string.error_insert_title
                    }

                    return@launch
                }

                ProjectValidatorResult.Success -> Unit
            }


            val endDate = addProject.endDate.takeIf { screenState.value.useEndDate }

            projectRepository.updateProject(
                projectId = projectToEditId!!,
                title = addProject.projectTitle.trim(),
                description = addProject.projectDescription.trim().ifBlank { null },
                startDate = addProject.startDate,
                endDate = endDate
            )

            _goBack.update { true }

        }
    }


    fun onErrorShown() {
        _error.update { null }
    }

    fun onGoneBack() {
        _goBack.update { false }
    }

}