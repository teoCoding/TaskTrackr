package com.teocoding.tasktrackr.presentation.screen.task_details

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.model.Photo
import com.teocoding.tasktrackr.domain.repository.PhotoRepository
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val photoRepository: PhotoRepository,
    projectRepository: ProjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _screenState = MutableStateFlow(TaskDetailsState())
    val screenState: StateFlow<TaskDetailsState> = _screenState.asStateFlow()

    private val taskId =
        checkNotNull(
            savedStateHandle.get<Long>(TaskRoute.Details.TASK_ID)
        )

    private val task = taskRepository.getTaskById(taskId)
        .onStart {
            _screenState.update { state ->
                state.copy(
                    isLoading = true
                )
            }
        }
        .filterNotNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskProject = task.flatMapLatest { task ->
        projectRepository.getProjectById(task.projectId)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskPhotos = task.flatMapLatest { task ->
        photoRepository.getPhotosWhereTask(task.id!!)
    }

    init {

        combine(
            task,
            taskProject,
            taskPhotos,
        ) { task, project, photos ->

            _screenState.update { state ->

                state.copy(
                    task = task,
                    taskProject = project,
                    photos = photos,
                    isLoading = false
                )
            }
        }
            .launchIn(viewModelScope)
    }


    fun onEvent(event: TaskDetailsEvent) {

        when (event) {

            TaskDetailsEvent.DeleteTask -> viewModelScope.launch {
                taskRepository.deleteTask(taskId)
            }


            TaskDetailsEvent.SwitchCompletedState -> {

                viewModelScope.launch {

                    taskRepository.updateCompleted(
                        taskId = taskId,
                        isCompleted = !_screenState.value.task!!.isCompleted
                    )
                }
            }

            is TaskDetailsEvent.DeletePhoto -> {
                deletePhoto(event.photo)
            }
        }
    }


    fun savePhoto(uri: Uri) {
        viewModelScope.launch {

            photoRepository.insertPhoto(
                taskId = taskId,
                localPath = uri.toString()
            )
        }
    }

    private fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            photoRepository.deletePhoto(photo)
        }
    }
}
