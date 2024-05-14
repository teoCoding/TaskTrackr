package com.teocoding.tasktrackr.domain.filter


fun <T : Filterable> Sequence<T>.applyFilterForComplete(filter: Filter): Sequence<T> {

    return this.filter { t -> t.isInFilter(filter) }
}