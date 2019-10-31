package com.example.mobilelab.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.database.data.CategoryExtension

@Dao
interface CategoryExtensionDao {

    @Query("SELECT * FROM CategoryExtension WHERE task_owner == :taskOwner")
    fun queryCategories(
        taskOwner: String
    ): List<CategoryExtension>

    @Insert
    fun insertCategory(
        categoryExtension: CategoryExtension
    )

    @Delete
    fun deleteCategory(
        categoryExtension: CategoryExtension
    )

}