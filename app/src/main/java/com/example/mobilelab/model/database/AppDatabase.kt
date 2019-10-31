package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilelab.model.database.dao.CategoryExtensionDao
import com.example.mobilelab.model.database.dao.PriorityExtensionDao
import com.example.mobilelab.model.database.dao.TaskExtensionDao
import com.example.mobilelab.model.database.data.CategoryExtension
import com.example.mobilelab.model.database.data.PriorityExtension
import com.example.mobilelab.model.database.data.TaskExtension

@Database(entities = [TaskExtension::class, CategoryExtension::class, PriorityExtension::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskExtensionDao(): TaskExtensionDao

    abstract fun categoryExtensionDao(): CategoryExtensionDao

    abstract fun priorityExtensionDao(): PriorityExtensionDao

}