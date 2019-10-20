package com.example.mobilelab.almostView.taskList

import com.example.mobilelab.model.data.TaskData

interface TaskListInterface {

    fun onFloatingActionButtonClick()

    fun initRecyclerView(
        taskDataList: ArrayList<TaskData>
    )

}