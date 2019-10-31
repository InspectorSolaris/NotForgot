package com.example.mobilelab.view.taskList

import com.example.mobilelab.model.taskData.Task

interface TaskListInterface {

    fun onFloatingActionButtonClick()

    fun initRecyclerView(
        taskDataList: ArrayList<Task>
    )

    fun finishActivity()

}