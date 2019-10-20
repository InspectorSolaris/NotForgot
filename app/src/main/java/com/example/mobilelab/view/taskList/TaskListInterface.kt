package com.example.mobilelab.view.taskList

import com.example.mobilelab.model.Data
import com.example.mobilelab.model.DataList

interface TaskListInterface {

    fun onFloatingActionButtonClick()

    fun initRecyclerView(
        dataList: DataList
    )

}