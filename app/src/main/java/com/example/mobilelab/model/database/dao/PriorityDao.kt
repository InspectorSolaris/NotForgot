package com.example.mobilelab.model.database.dao

import androidx.room.*
import com.example.mobilelab.model.taskData.Priority

@Dao
interface PriorityDao {

    @Query("SELECT * FROM Priority")
    fun getPriorities(): List<Priority>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postPriorities(
        priorities: List<Priority>
    )

    @Delete
    fun deletePriorities(
        priorities: List<Priority>
    )

}