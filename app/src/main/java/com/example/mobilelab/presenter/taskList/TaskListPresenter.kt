package com.example.mobilelab.presenter.taskList

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
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

        }

        repository.getPriorities(
            sharedPreferencesHandler.readString(
                context.getString(R.string.shared_preferences_user_token)
            ),
            { _, _ ->

            }
        ) {

        }

        repository.getTasks(
            sharedPreferencesHandler.readString(
                context.getString(R.string.shared_preferences_user_token)
            ),
            { _, _ ->

            }
        ) {
            it.sortBy { task -> task.category.id }
        }
    }

    fun initRecyclerView() {

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