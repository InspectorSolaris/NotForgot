package com.example.mobilelab.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mobilelab.model.taskData.Priority

@Dao
interface PriorityDao {

    @Query("SELECT * FROM Priority")
    fun getPriorities(): List<Priority>

}