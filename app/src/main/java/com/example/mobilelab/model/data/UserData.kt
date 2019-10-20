package com.example.mobilelab.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
    var name: String,
    var email: String,
    var password: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)