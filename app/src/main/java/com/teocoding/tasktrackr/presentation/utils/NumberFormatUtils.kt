package com.teocoding.tasktrackr.presentation.utils

import java.text.NumberFormat
import java.util.Locale


fun Locale.getPercentFormat(): NumberFormat {

    return NumberFormat.getPercentInstance(this)
}

fun Locale.getIntegerFormat(): NumberFormat {

    return NumberFormat.getIntegerInstance(this)
}