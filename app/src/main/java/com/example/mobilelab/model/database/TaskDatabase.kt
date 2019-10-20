package com.example.mobilelab.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilelab.model.data.TaskData

@Database(entities = [TaskData::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDataDao() : TaskDataDao

}