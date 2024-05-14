package com.teocoding.tasktrackr.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teocoding.tasktrackr.data.local.db.model.TaskDb
import com.teocoding.tasktrackr.data.local.db.utils.UpdateCompletedTask
import com.teocoding.tasktrackr.data.local.db.utils.UpdateTask
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao {

    @Query("SELECT * FROM task")
    abstract fun getAllTasks(): Flow<List<TaskDb>>

    @Query("DELETE FROM task WHERE id = :taskId")
    abstract suspend fun deleteTask(taskId: Long)

    @Insert
    abstract suspend fun insertTask(task: TaskDb)

    @Update(entity = TaskDb::class)
    abstract suspend fun updateCompletedTask(updateTask: UpdateCompletedTask)

    @Update(entity = TaskDb::class)
    abstract suspend fun updateTask(updateTask: UpdateTask)

    @Query("SELECT * FROM task WHERE project_id = :projectId")
    abstract fun getTaskByProject(projectId: Long): Flow<List<TaskDb>>


    @Query("SELECT * FROM task WHERE id = :taskId")
    abstract fun getTaskById(taskId: Long): Flow<TaskDb?>

    @Query("SELECT * FROM task ORDER BY CASE WHEN end_date IS NULL THEN 1 ELSE 0 END ASC")
    abstract fun getTasksSortByEndDate(): Flow<List<TaskDb>>

    @Query("SELECT * FROM task WHERE title LIKE '%' || :query || '%'")
    abstract fun queryTasksByName(query: String): Flow<List<TaskDb>>

    @Query("SELECT * FROM task WHERE end_date = :date")
    abstract fun getTasksThatExpireAt(date: Long): Flow<List<TaskDb>>

}