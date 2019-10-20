package com.example.mobilelab.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.data.TaskData

@Dao
interface TaskDataDao {

    @Query("SELECT * FROM TaskData")
    fun getAllTaskData() : ArrayList<TaskData>

    @Insert
    fun addTaskData(taskData: TaskData)

}