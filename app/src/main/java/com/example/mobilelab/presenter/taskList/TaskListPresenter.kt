package com.example.mobilelab.presenter.taskList

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListAdapter
import com.example.mobilelab.view.taskList.TaskListActivity
import com.example.mobilelab.view.taskList.TaskListInterface

class TaskListPresenter(
    private var taskListView: TaskListInterface?
) {

    private val context = taskListView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private lateinit var taskListAdapter: TaskListAdapter

    fun onDestroy() {
        taskListView = null
    }

    fun onFloatingActionButtonClick() {
        taskListView?.onFloatingActionButtonClick()
    }

    fun onExitButtonClick() {
        sharedPreferencesHandler.saveString(
            context.getString(R.string.shared_preferences_user_token),
            context.getString(R.string.shared_preferences_null_token)
        )

        taskListView?.finishActivity()
    }

    fun initData() {
        val token = sharedPreferencesHandler.readString(
            context.getString(R.string.shared_preferences_user_token)
        )

        Repository.getCategories(
            token,
            { _, _ ->
            }
        ) { _, _ ->
        }

        Repository.getPriorities(
            token,
            { _, _ ->
            }
        ) { _, _ ->
        }

        Repository.getTasks(
            token,
            { _, _ ->
            }
        ) { _, _ ->
            Repository.getTasksData().filter { task -> task.category == null }.forEach { task -> task.category = Category(-1, "NULL") }
            Repository.getTasksData().filter { task -> task.priority == null }.forEach { task -> task.priority = Priority(-1, "NULL", "#000000") }
            Repository.getTasksData().sortBy { T -> T.category?.id }

            taskListView?.initRecyclerView()
        }
    }

    fun setTaskListAdapter(
        taskListAdapter: TaskListAdapter
    ) {
        this.taskListAdapter = taskListAdapter
    }

    fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        intent?.putExtra(TaskListActivity.REQUEST_CODE_STRING, requestCode)
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if(resultCode != Activity.RESULT_OK) {
            return
        }

        taskListAdapter.dataAdded()
    }

}