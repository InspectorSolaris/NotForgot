package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.data.UserData

@Database(entities = [TaskData::class, UserData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDataDao(): TaskDataDao

    abstract fun userDataDao(): UserDataDao
}