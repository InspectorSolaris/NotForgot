package com.example.mobilelab.model.taskData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val done: Int,
    val created: Long,
    val deadline: Long,
    var category: Category?,
    var priority: Priority?
)