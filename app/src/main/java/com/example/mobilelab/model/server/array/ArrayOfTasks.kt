package com.example.mobilelab.model.server.array

import com.example.mobilelab.model.server.task.TaskId

data class ArrayOfTasks(
    val tasks: ArrayList<TaskId>
)