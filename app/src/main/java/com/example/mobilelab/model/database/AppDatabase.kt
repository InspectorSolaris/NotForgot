package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilelab.model.database.UserDataDao

@Database(entities = [UserDataDao::class, TaskDataDao::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDataDao(): UserDataDao

    abstract fun taskDataDao(): TaskDataDao

}