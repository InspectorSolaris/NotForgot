package com.example.mobilelab.model.database.dao

import androidx.room.*
import com.example.mobilelab.model.taskData.TaskExt

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskExt WHERE token == :token")
    fun getTasks(
        token: String
    ): List<TaskExt>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postTask(
        taskExt: TaskExt
    )

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun patchTask(
        taskExt: TaskExt
    )

    @Delete
    fun deleteTask(
        taskExt: TaskExt
    )

}