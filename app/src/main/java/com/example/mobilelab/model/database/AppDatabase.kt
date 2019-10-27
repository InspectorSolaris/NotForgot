package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.data.UserData

@Database(entities = [UserData::class, TaskData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDataDao(): UserDataDao

    abstract fun taskDataDao(): TaskDataDao

}