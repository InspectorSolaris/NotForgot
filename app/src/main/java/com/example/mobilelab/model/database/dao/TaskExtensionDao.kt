package com.example.mobilelab.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.database.data.TaskExtension

@Dao
interface TaskExtensionDao {

    @Query("SELECT * FROM TaskExtension WHERE task_owner == :taskOwner")
    fun queryTasks(
        taskOwner: String
    ): List<TaskExtension>

    @Insert
    fun insertTask(
        taskExtension: TaskExtension
    )

    @Delete
    fun deleteTask(
        taskExtension: TaskExtension
    )

}