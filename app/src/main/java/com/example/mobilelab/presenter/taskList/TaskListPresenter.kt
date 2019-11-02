package com.example.mobilelab.presenter.taskList

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.model.taskData.Task
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListAdapter
import com.example.mobilelab.view.taskList.TaskListActivity
import com.example.mobilelab.view.taskList.TaskListInterface

class TaskListPresenter(
    private var taskListView: TaskListInterface?
) {

    private val context = taskListView as Context
    private val repository = Repository()
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var categoriesData: ArrayList<Category>
    private lateinit var prioritiesData: ArrayList<Priority>
    private lateinit var tasksData: ArrayList<Task>

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
        repository.getCategories(
            sharedPreferencesHandler.readString(
                context.getString(R.string.shared_preferences_user_token)
            ),
            { _, _ ->
            }
        ) {
            categoriesData = it
        }

        repository.getPriorities(
            sharedPreferencesHandler.readString(
                context.getString(R.string.shared_preferences_user_token)
            ),
            { _, _ ->
            }
        ) {
            prioritiesData = it
        }

        repository.getTasks(
            sharedPreferencesHandler.readString(
                context.getString(R.string.shared_preferences_user_token)
            ),
            { _, _ ->
            }
        ) {
            tasksData = it
            tasksData.filter { task -> task.category == null }.forEach { task -> task.category = Category(-1, "NULL") }
            tasksData.filter { task -> task.priority == null }.forEach { task -> task.priority = Priority(-1, "NULL", "#000000") }
            tasksData.sortBy { T -> T.category?.id }

            taskListView?.initRecyclerView(tasksData)
        }
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

    fun onStartActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        intent?.putExtra(TaskListActivity.REQUEST_CODE_STRING, requestCode)
        intent?.putExtra(TaskListActivity.TASK_CATEGORIES, categoriesData)
        intent?.putExtra(TaskListActivity.TASK_PRIORITIES, prioritiesData)
    }

}