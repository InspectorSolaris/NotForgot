package com.example.mobilelab.view.taskDetails

import android.content.Intent

interface TaskDetailsInterface {

    fun onEditButtonClick()

    fun setTaskTitle(
        title: String
    )

    fun setTaskDescription(
        description: String
    )

    fun setTaskState(
        state: String,
        color: Int
    )

    fun setTaskCreated(
        created: String
    )

    fun setTaskDeadline(
        deadline: String
    )

    fun setTaskCategory(
        category: String
    )

    fun setTaskPriority(
        priority: String,
        color: Int
    )

    fun finishActivity()

    fun finishActivityWithResult(
        intent: Intent
    )

}