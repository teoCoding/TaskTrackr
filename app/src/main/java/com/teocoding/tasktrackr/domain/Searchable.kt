package com.teocoding.tasktrackr.domain

interface Searchable {

    fun containsQuery(query: String): Boolean
}