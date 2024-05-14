package com.teocoding.tasktrackr.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teocoding.tasktrackr.data.local.db.model.PhotoDb
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PhotoDao {

    @Insert
    abstract suspend fun insertPhoto(photoDb: PhotoDb)

    @Query("SELECT * FROM photo WHERE task_id = :taskId")
    abstract fun getPhotosWhereTaskId(taskId: Long): Flow<List<PhotoDb>>

    @Query("DELETE FROM photo WHERE id = :photoId")
    abstract suspend fun deletePhoto(photoId: Long)


}