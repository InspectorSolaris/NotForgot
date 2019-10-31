package com.example.mobilelab.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.database.data.PriorityExtension

@Dao
interface PriorityExtensionDao {

    @Query("SELECT * FROM PriorityExtension")
    fun queryPriorities(

    ): List<PriorityExtension>

    @Insert
    fun insertPriority(
        priorityExtension: PriorityExtension
    )

    @Delete
    fun deletePriority(
        priorityExtension: PriorityExtension
    )

}