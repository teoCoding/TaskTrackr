package com.teocoding.tasktrackr.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "photo",
    indices = [
        Index(value = ["task_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TaskDb::class, parentColumns = ["id"], childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhotoDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "local_path")
    val localPath: String,
    @ColumnInfo(name = "task_id")
    val taskId: Long
)
