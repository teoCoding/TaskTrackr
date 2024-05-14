package com.teocoding.tasktrackr.domain

fun <T : Searchable> Sequence<T>.applyQuery(query: String): Sequence<T> {

    return this.filter { t -> t.containsQuery(query) }
}