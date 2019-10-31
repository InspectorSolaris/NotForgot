package com.example.mobilelab.model.server.task

data class TaskId(
    val id: Int,
    val title: String,
    val description: String,
    val done: Int,
    val deadline: Int,
    val category_id: Int,
    val priority_id: Int
)