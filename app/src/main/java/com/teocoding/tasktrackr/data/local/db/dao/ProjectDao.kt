package com.teocoding.tasktrackr.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teocoding.tasktrackr.data.local.db.model.ProjectDb
import com.teocoding.tasktrackr.data.local.db.utils.UpdateCompletedProject
import com.teocoding.tasktrackr.data.local.db.utils.UpdateProject
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProjectDao {

    @Query("SELECT * FROM project")
    abstract fun getAllProjects(): Flow<List<ProjectDb>>

    @Query("DELETE FROM project WHERE id = :projectId")
    abstract suspend fun deleteProject(projectId: Long)

    @Insert
    abstract suspend fun insertProject(projectDb: ProjectDb)

    @Update(entity = ProjectDb::class)
    abstract suspend fun updateCompletedProject(updateProject: UpdateCompletedProject)

    @Query("SELECT * FROM project WHERE id = :projectId")
    abstract fun getProjectById(projectId: Long): Flow<ProjectDb?>

    @Query("SELECT * FROM project ORDER BY end_date ASC ")
    abstract fun getProjectsSortByEndDate(): Flow<List<ProjectDb>>

    @Query("SELECT * FROM project WHERE title LIKE '%' || :query || '%'")
    abstract fun queryProjectsByName(query: String): Flow<List<ProjectDb>>

    @Update(entity = ProjectDb::class)
    abstract suspend fun updateProject(updateProject: UpdateProject)

    @Query("SELECT * FROM project WHERE end_date = :date")
    abstract fun getProjectsThatExpireAt(date: Long): Flow<List<ProjectDb>>

}