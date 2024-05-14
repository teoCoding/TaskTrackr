package com.teocoding.tasktrackr.domain.model

import com.teocoding.tasktrackr.domain.Searchable
import com.teocoding.tasktrackr.domain.filter.Filter
import com.teocoding.tasktrackr.domain.filter.Filterable

data class ProjectWithStats(
    val project: Project,
    val stats: ProjectStats
) : Filterable, Searchable {

    override fun isInFilter(filter: Filter): Boolean {


        return when (filter) {
            Filter.All -> true
            Filter.Completed -> project.isCompleted
            Filter.NotCompleted -> !project.isCompleted
        }
    }

    override fun containsQuery(query: String): Boolean {
        return this.project.title.contains(
            other = query,
            ignoreCase = true
        )
    }
}
