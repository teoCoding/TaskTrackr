package com.teocoding.tasktrackr.domain.model

import android.os.Parcelable
import com.teocoding.tasktrackr.domain.Sortable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Project(

    val id: Long?,
    val title: String,
    val description: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val completedDate: LocalDate?,
    val isCompleted: Boolean
) : Parcelable, Sortable {

    override fun byTitle(): String {
        return this.title
    }
}
