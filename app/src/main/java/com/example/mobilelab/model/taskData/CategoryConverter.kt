package com.example.mobilelab.model.taskData

import androidx.room.TypeConverter

class CategoryConverter {

    @TypeConverter
    fun fromCategory(
        category: Category
    ): String {
        return "${category.id}///${category.name}"
    }

    @TypeConverter
    fun stringToCategory(
        string: String
    ): Category {
        val args = string.split("///")
        return Category(args[0].toInt(), args[1])
    }

}