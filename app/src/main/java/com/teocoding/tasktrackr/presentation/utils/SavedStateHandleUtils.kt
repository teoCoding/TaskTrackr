package com.teocoding.tasktrackr.presentation.utils

import androidx.lifecycle.SavedStateHandle

/**
 * Updates the value stored in SavedStateHandle with the provided key.
 * @param key The key associated with value in SavedStateHandle.
 * @param updateFunction A lambda function that takes the current value of the value and returns the updated value.
 */
fun <T> SavedStateHandle.update(
    key: String,
    updateFunction: (value: T?) -> T?,
) {
    val currentValue = this.get<T>(key)

    val newValue = updateFunction(currentValue)

    this[key] = newValue
}