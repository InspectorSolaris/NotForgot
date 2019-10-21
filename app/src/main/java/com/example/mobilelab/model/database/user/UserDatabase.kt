package com.example.mobilelab.model.database.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDataDao::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDataDao(): UserDataDao

}