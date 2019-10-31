package com.example.mobilelab.model.taskData

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val done: Int,
    val created: Int,
    val deadline: Int,
    var category: Category?,
    var priority: Priority?
)