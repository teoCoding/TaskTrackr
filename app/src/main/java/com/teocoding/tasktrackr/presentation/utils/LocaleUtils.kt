package com.teocoding.tasktrackr.presentation.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import java.util.Locale

fun Context.getCurrentLocale(): Locale {
    val locales = AppCompatDelegate.getApplicationLocales()

    if (locales.isEmpty) {
        return ConfigurationCompat.getLocales(resources.configuration)[0]
            ?: Locale.getDefault()
    }

    return locales[0] ?: ConfigurationCompat.getLocales(resources.configuration)[0]
    ?: Locale.getDefault()

}