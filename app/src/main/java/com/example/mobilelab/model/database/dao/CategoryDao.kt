package com.example.mobilelab.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilelab.model.taskData.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postCategory(
        categories: List<Category>
    )

}