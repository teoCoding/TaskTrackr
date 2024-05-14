package com.teocoding.tasktrackr.presentation.screen.task_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.applyQuery
import com.teocoding.tasktrackr.domain.filter.applyFilterForComplete
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TaskMainViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(TaskMainState())
    val screenState: StateFlow<TaskMainState> = _screenState.asStateFlow()

    private val tasks = taskRepository.getAllTasks()
        .onStart {
            _screenState.update { state ->
                state.copy(isLoading = true)
            }
        }
        .onEach {
            _screenState.update { state ->
                state.copy(isLoading = false)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _filter = _screenState.mapLatest {
        it.filter
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _searchText = _screenState.mapLatest {
        it.searchText
    }
        .debounce(300.milliseconds)


    init {
        combine(tasks, _filter, _searchText) { tasks, filter, searchText ->

            val filteredList = tasks
                .asSequence()
                .applyFilterForComplete(filter)
                .applyQuery(searchText)
                .toList()


            _screenState.update { state ->
                state.copy(
                    tasks = filteredList
                )

            }

        }
            .launchIn(viewModelScope)
    }


    fun onEvent(event: TaskMainEvent) {

        when (event) {
            is TaskMainEvent.OnSearchTextChange -> {
                _screenState.update { state ->
                    state.copy(
                        searchText = event.text
                    )

                }
            }

            is TaskMainEvent.OnFilterChange -> {
                _screenState.update { state ->
                    state.copy(
                        filter = event.filter
                    )

                }
            }

            is TaskMainEvent.OnUpdateCompleted -> {
                updateCompleted(
                    taskId = event.id,
                    isCompleted = event.isCompleted
                )
            }
        }
    }

    fun updateCompleted(
        taskId: Long,
        isCompleted: Boolean
    ) {

        viewModelScope.launch {
            taskRepository.updateCompleted(
                taskId, isCompleted
            )
        }

    }
}