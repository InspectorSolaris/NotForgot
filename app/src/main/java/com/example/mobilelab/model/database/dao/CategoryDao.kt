package com.example.mobilelab.model.database.dao

import androidx.room.*
import com.example.mobilelab.model.taskData.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun postCategories(
        categories: List<Category>
    )

    @Delete
    fun deleteCategories(
        categories: List<Category>
    )

}