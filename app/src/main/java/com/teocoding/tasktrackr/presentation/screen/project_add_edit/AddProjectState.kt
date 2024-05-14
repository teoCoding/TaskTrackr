package com.teocoding.tasktrackr.presentation.screen.project_add_edit

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Immutable
data class AddProjectState(
    val projectTitle: String = "",
    val projectDescription: String = "",
    val startDate: LocalDate? = LocalDate.now(),
    val endDate: LocalDate? = null,
    val useEndDate: Boolean = false
) : Parcelable