package com.example.mobilelab.model.taskData

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val done: Int,
    val created: Long,
    val deadline: Long,
    var category: Category?,
    var priority: Priority?
)