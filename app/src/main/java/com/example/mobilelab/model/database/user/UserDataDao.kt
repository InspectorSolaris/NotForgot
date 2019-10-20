package com.example.mobilelab.model.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.data.UserData

@Dao
interface UserDataDao {

    @Query("SELECT * FROM UserData")
    fun getAllUserData(): ArrayList<UserData>

    @Insert
    fun addUserData(userData: UserData)

}