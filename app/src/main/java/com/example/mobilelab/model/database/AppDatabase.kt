package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mobilelab.model.database.dao.CategoryDao
import com.example.mobilelab.model.database.dao.PriorityDao
import com.example.mobilelab.model.database.dao.TaskDao
import com.example.mobilelab.model.taskData.*

@Database(entities = [Category::class, Priority::class, Task::class], version = 2)
@TypeConverters(CategoryConverter::class, PriorityConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun priorityDao(): PriorityDao

    abstract fun taskDao(): TaskDao

}