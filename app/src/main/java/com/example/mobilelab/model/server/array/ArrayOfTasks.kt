package com.example.mobilelab.model.server.array

import com.example.mobilelab.model.server.form.TaskForm

data class ArrayOfTasks(
    val tasks: ArrayList<TaskForm>
)