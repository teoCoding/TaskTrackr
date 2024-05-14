package com.teocoding.tasktrackr.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.repository.ProjectRepository
import com.teocoding.tasktrackr.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var searchJob: Job? = null

    private val _screenState = MutableStateFlow(SearchScreenState())
    val screenState: StateFlow<SearchScreenState> = _screenState.asStateFlow()


    fun onSearchTextChange(text: String) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {

            _screenState.update { state ->
                state.copy(
                    searchText = text
                )
            }


            if (text.isBlank()) {
                _screenState.update { state ->
                    state.copy(
                        queryResult = emptyList(),
                    )
                }

                return@launch
            }

            delay(300)

            val projectFiltered = async {

                projectRepository.getProjectsThatTitleContains(text).first()
            }


            val taskFiltered = async {

                taskRepository.getTaskThatTitleContains(text).first()
            }


            val sortedList =
                (projectFiltered.await() + taskFiltered.await())
                    .sortedBy {
                        it.byTitle()
                    }

            _screenState.update { state ->
                state.copy(
                    queryResult = sortedList
                )

            }

        }
    }

}

