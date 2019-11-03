package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilelab.model.database.dao.CategoryDao
import com.example.mobilelab.model.database.dao.PriorityDao
import com.example.mobilelab.model.database.dao.TaskDao
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.model.taskData.TaskExt

@Database(entities = [Category::class, Priority::class, TaskExt::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun priorityDao(): PriorityDao

    abstract fun taskDao(): TaskDao

}