package com.teocoding.tasktrackr.domain.model

import com.teocoding.tasktrackr.domain.Priority
import com.teocoding.tasktrackr.domain.Searchable
import com.teocoding.tasktrackr.domain.Sortable
import com.teocoding.tasktrackr.domain.filter.Filter
import com.teocoding.tasktrackr.domain.filter.Filterable
import java.time.LocalDate

data class Task(
    val id: Long?,
    val title: String,
    val description: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val completedDate: LocalDate?,
    val priority: Priority,
    val isCompleted: Boolean,
    val projectId: Long
) : Filterable, Searchable, Sortable {

    override fun isInFilter(filter: Filter): Boolean {

        return when (filter) {
            Filter.All -> true
            Filter.Completed -> isCompleted
            Filter.NotCompleted -> !isCompleted
        }
    }

    override fun containsQuery(query: String): Boolean {

        return this.title.contains(
            other = query,
            ignoreCase = true
        )
    }

    override fun byTitle(): String {
        return this.title
    }
}