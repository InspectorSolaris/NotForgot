package com.example.mobilelab.presenter.taskList

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.array.ArrayOfTasks
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListAdapter
import com.example.mobilelab.view.taskList.TaskListInterface

class TaskListPresenter(
    private var taskListView: TaskListInterface?,
    applicationContext: Context
) {

    private val context = taskListView as Context
    private val repository: Repository = Repository(applicationContext)
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    fun onDestroy() {
        taskListView = null
    }

    fun onFloatingActionButtonClick() {
        taskListView?.onFloatingActionButtonClick()
    }

    fun initRecyclerView() {
//        repository.getAllTaskData(
//            sharedPreferencesHandler.readString(
//                context.getString(R.string.shared_preferences_user_key)
//            )
//        ) {
//            taskListView?.initRecyclerView(it)
//        }
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