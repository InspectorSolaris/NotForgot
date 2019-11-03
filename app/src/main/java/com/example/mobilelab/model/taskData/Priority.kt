package com.example.mobilelab.model.taskData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Priority(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val color: String
)