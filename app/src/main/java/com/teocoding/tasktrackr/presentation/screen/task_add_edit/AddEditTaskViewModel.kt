package com.teocoding.tasktrackr.presentation.screen.task_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.R
import com.teocoding.tasktrackr.domain.InsertTaskValidator
import com.teocoding.tasktrackr.domain.TaskValidatorResult
import com.teocoding.tasktrackr.domain.model.Task
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import com.teocoding.tasktrackr.presentation.utils.update
import com.teocoding.tasktrackr.ui.navigation.task.TaskRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val SCREEN_STATE_KEY = "add_task_screen_state"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val insertTaskValidator: InsertTaskValidator
) : ViewModel() {

    val screenState = savedStateHandle.getStateFlow(SCREEN_STATE_KEY, AddEditTaskState())

    private val _error = MutableStateFlow<Int?>(null)
    val error: StateFlow<Int?> = _error.asStateFlow()

    private val _goBack = MutableStateFlow(false)
    val goBack: StateFlow<Boolean> = _goBack.asStateFlow()

    private val taskToEdit =
        savedStateHandle.get<Long>(
            TaskRoute.AddEditTask.TASK_ID
        ).takeIf { id ->
            id != TaskRoute.AddEditTask.DEFAULT_VALUE_TASK_ID
        }

    init {

        projectRepository.getAllProjects()
            .onEach { projects ->
                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        projects = projects
                    )
                }
            }
            .launchIn(viewModelScope)


        taskToEdit?.let { taskId ->

            val taskToEdit = taskRepository.getTaskById(taskId)
                .filterNotNull()

            val taskProject = taskToEdit
                .flatMapLatest { task ->

                    projectRepository.getProjectById(task.projectId)
                }

            taskToEdit.combine(taskProject) { task, project ->

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        taskTitle = task.title,
                        taskDescription = task.description ?: "",
                        startDate = task.startDate,
                        endDate = task.endDate,
                        priority = task.priority,
                        selectedProject = project,
                        useEndDate = task.endDate != null
                    )
                }

            }
                .launchIn(viewModelScope)
        }
    }


    fun onEvent(event: AddTaskEvent) {
        when (event) {
            is AddTaskEvent.OnDescriptionChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        taskDescription = event.text
                    )
                }
            }

            is AddTaskEvent.OnEndDateChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        endDate = event.date
                    )
                }
            }

            is AddTaskEvent.OnSelectedProjectChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        selectedProject = event.project
                    )
                }
            }

            is AddTaskEvent.OnStartDateChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        startDate = event.date
                    )
                }
            }

            is AddTaskEvent.OnTitleChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        taskTitle = event.text
                    )
                }
            }

            is AddTaskEvent.OnUseEndDateChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        useEndDate = event.useEndDate
                    )
                }
            }

            is AddTaskEvent.OnPriorityChange -> {

                savedStateHandle.update<AddEditTaskState>(
                    key = SCREEN_STATE_KEY
                ) { state ->
                    state?.copy(
                        priority = event.priority
                    )
                }
            }

            AddTaskEvent.OnSaveTask -> {

                if (taskToEdit == null) {

                    insertTask()
                } else {

                    updateTask()
                }
            }
        }
    }


    private fun insertTask() {
        viewModelScope.launch {

            val addTask = screenState.value

            val validationResult = insertTaskValidator.execute(
                project = addTask.selectedProject,
                taskTitle = addTask.taskTitle
            )

            when (validationResult) {
                TaskValidatorResult.BlankTitle -> {

                    _error.update {
                        R.string.error_insert_title
                    }

                    return@launch
                }

                TaskValidatorResult.NullProject -> {

                    _error.update {
                        R.string.error_choose_project
                    }

                    return@launch
                }

                TaskValidatorResult.Success -> Unit
            }

            val endDate = addTask.endDate.takeIf { screenState.value.useEndDate }

            val task = Task(
                id = null,
                title = addTask.taskTitle.trim(),
                description = addTask.taskDescription.trim().ifBlank { null },
                startDate = addTask.startDate,
                endDate = endDate,
                completedDate = null,
                priority = addTask.priority,
                isCompleted = false,
                projectId = addTask.selectedProject!!.id!!
            )

            taskRepository.insertTask(task)

            _goBack.update { true }
        }

    }

    private fun updateTask() {
        viewModelScope.launch {

            val addTask = screenState.value

            val validationResult = insertTaskValidator.execute(
                project = addTask.selectedProject,
                taskTitle = addTask.taskTitle
            )

            when (validationResult) {
                TaskValidatorResult.BlankTitle -> {

                    _error.update {
                        R.string.error_insert_title
                    }

                    return@launch
                }

                TaskValidatorResult.NullProject -> {

                    _error.update {
                        R.string.error_choose_project
                    }

                    return@launch
                }

                TaskValidatorResult.Success -> Unit
            }

            val endDate = addTask.endDate.takeIf { screenState.value.useEndDate }

            taskRepository.updateTask(
                taskId = taskToEdit!!,
                title = addTask.taskTitle.trim(),
                description = addTask.taskDescription.trim().ifBlank { null },
                startDate = addTask.startDate,
                endDate = endDate,
                priority = addTask.priority,
                projectId = addTask.selectedProject!!.id!!
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