package com.teocoding.tasktrackr.presentation.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

fun LocalDate.toUtcTimeMillis(): Long {

    return this.atStartOfDay().atOffset(ZoneOffset.UTC)
        .toInstant().toEpochMilli()
}

fun Instant.toUtcLocalDate(): LocalDate {

    return this.atOffset(ZoneOffset.UTC).toLocalDate()
}