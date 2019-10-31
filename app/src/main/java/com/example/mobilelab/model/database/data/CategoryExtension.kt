package com.example.mobilelab.model.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryExtension(
    val id: Int,
    val name: String,
    val task_owner: String,
    @PrimaryKey(autoGenerate = true) val primary_key: Int
)