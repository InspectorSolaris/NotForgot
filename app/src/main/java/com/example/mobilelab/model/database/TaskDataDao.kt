package com.example.mobilelab.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.data.TaskData

@Dao
interface TaskDataDao {

    @Query("SELECT * FROM TaskData WHERE taskOwner == :user")
    fun getAllTaskData(
        user: String
    ): List<TaskData>

    @Insert
    fun addTaskData(
        taskData: TaskData
    )

    @Delete
    fun deleteTaskData(
        taskData: TaskData
    )

}