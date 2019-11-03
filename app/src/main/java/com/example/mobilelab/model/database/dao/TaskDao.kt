package com.example.mobilelab.model.database.dao

import androidx.room.*
import com.example.mobilelab.model.taskData.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postTasks(
        task: List<Task>
    )

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun patchTask(
        task: Task
    )

    @Delete
    fun deleteTask(
        task: Task
    )

    @Delete
    fun deleteTasks(
        tasks: List<Task>
    )

}