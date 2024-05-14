package com.teocoding.tasktrackr.presentation.screen.project_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teocoding.tasktrackr.domain.applyQuery
import com.teocoding.tasktrackr.domain.filter.applyFilterForComplete
import com.teocoding.tasktrackr.domain.use_case.GetProjectsWithStats
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
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProjectMainViewModel @Inject constructor(
    getProjectsWithStats: GetProjectsWithStats
) : ViewModel() {

    private val _screenState = MutableStateFlow(ProjectMainState())
    val screenState: StateFlow<ProjectMainState> = _screenState.asStateFlow()


    private val projects = getProjectsWithStats.execute()
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

    init {

        @OptIn(ExperimentalCoroutinesApi::class)
        val filter = _screenState.mapLatest {
            it.filter
        }

        @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
        val searchText = _screenState.mapLatest {
            it.searchText
        }
            .debounce(300.milliseconds)


        combine(projects, filter, searchText) { projects, currentFilter, currentSearchText ->

            val filteredList = projects
                .asSequence()
                .applyFilterForComplete(currentFilter)
                .applyQuery(currentSearchText)
                .toList()


            _screenState.update { state ->
                state.copy(
                    projects = filteredList
                )

            }

        }
            .launchIn(viewModelScope)
    }


    fun onEvent(event: ProjectMainEvent) {

        when (event) {
            is ProjectMainEvent.OnSearchTextChange -> {
                _screenState.update { state ->
                    state.copy(
                        searchText = event.text
                    )

                }
            }

            is ProjectMainEvent.OnFilterChange -> {
                _screenState.update { state ->
                    state.copy(
                        filter = event.filter
                    )

                }
            }
        }
    }

}