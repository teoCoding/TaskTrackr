package com.teocoding.tasktrackr.domain.repository

import com.teocoding.tasktrackr.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun insertPhoto(
        taskId: Long,
        localPath: String
    )

    fun getPhotosWhereTask(taskId: Long): Flow<List<Photo>>

    suspend fun deletePhoto(photo: Photo)
}