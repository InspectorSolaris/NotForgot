package com.example.mobilelab.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mobilelab.model.data.UserData

@Dao
interface UserDataDao {

    @Query("SELECT * FROM userdata")
    fun getAllUserData(): List<UserData>

    @Insert
    fun addUserData(
        userData: UserData
    )

}