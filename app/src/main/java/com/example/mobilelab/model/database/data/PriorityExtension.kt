package com.example.mobilelab.model.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriorityExtension(
    val id: Int,
    val name: String,
    val color: String,
    @PrimaryKey(autoGenerate = true) val primary_key: Int
)