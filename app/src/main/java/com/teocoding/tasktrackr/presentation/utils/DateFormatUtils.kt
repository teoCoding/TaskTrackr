package com.teocoding.tasktrackr.presentation.utils

import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.time.format.FormatStyle
import java.util.Locale


fun Locale.getShortStyleFormatter(): DateTimeFormatter {

    val decimalStyle = DecimalStyle.of(this)

    return DateTimeFormatter
        .ofLocalizedDate(FormatStyle.SHORT)
        .withLocale(this)
        .withDecimalStyle(decimalStyle)
}

fun Locale.getLongStyleFormatter(): DateTimeFormatter {

    val decimalStyle = DecimalStyle.of(this)

    return DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)
        .withLocale(this)
        .withDecimalStyle(decimalStyle)
}