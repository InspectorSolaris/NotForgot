package com.example.mobilelab.presenter.taskList

import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListAdapter
import com.example.mobilelab.view.taskList.TaskListInterface

class TaskListPresenter(
    private var taskListView: TaskListInterface?
) {

    private var taskListAdapter: TaskListAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    fun onDestroy() {
        taskListView = null
    }

    fun onFloatingActionButtonClick() {
        taskListView?.onFloatingActionButtonClick()
    }

    fun initRecyclerView() {
//        taskListView?.initRecyclerView()
    }

    fun setTaskListAdapter(
        taskListAdapter: TaskListAdapter
    ) {
        this.taskListAdapter = taskListAdapter
    }

    fun setItemTouchHelper(
        itemTouchHelper: ItemTouchHelper
    ) {
        this.itemTouchHelper = itemTouchHelper
    }

}