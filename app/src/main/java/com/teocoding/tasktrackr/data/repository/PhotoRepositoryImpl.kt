package com.teocoding.tasktrackr.data.repository

import com.teocoding.tasktrackr.data.local.db.dao.PhotoDao
import com.teocoding.tasktrackr.data.local.db.model.PhotoDb
import com.teocoding.tasktrackr.domain.model.Photo
import com.teocoding.tasktrackr.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : PhotoRepository {

    override suspend fun insertPhoto(
        taskId: Long,
        localPath: String
    ) {

        val photoDb = PhotoDb(
            id = null,
            localPath = localPath,
            taskId = taskId
        )

        photoDao.insertPhoto(photoDb)

    }

    override fun getPhotosWhereTask(taskId: Long): Flow<List<Photo>> {
        return photoDao.getPhotosWhereTaskId(taskId)
            .map { list ->
                list.map { photoDb ->
                    Photo(
                        id = photoDb.id!!,
                        localPath = photoDb.localPath
                    )
                }
            }
    }

    override suspend fun deletePhoto(photo: Photo) {
        photoDao.deletePhoto(photo.id)
    }
}