package com.teocoding.tasktrackr.presentation.screen.search

import androidx.compose.runtime.Immutable
import com.teocoding.tasktrackr.domain.Sortable

@Immutable
data class SearchScreenState(
    val queryResult: List<Sortable> = emptyList(),
    val searchText: String = ""
)
