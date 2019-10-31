package com.example.mobilelab.model.database.data

import androidx.room.Entity

@Entity
data class TaskExtension(
    val title: String,
    val description: String,
    val done: Int,
    val deadline: Int,
    val category_id: Int,
    val priority_id: Int,
    val task_owner: String,
    val primary_key: Int
)