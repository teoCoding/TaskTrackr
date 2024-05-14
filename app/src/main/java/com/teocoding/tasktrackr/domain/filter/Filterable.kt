package com.teocoding.tasktrackr.domain.filter

interface Filterable {

    fun isInFilter(filter: Filter): Boolean
}