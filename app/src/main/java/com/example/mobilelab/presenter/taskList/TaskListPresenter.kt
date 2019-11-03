package com.example.mobilelab.presenter.taskList

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.database.AppDatabase
import com.example.mobilelab.model.server.form.TaskForm
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListAdapter
import com.example.mobilelab.view.taskDetails.TaskDetailsActivity
import com.example.mobilelab.view.taskEdit.TaskEditActivity
import com.example.mobilelab.view.taskList.TaskListActivity
import com.example.mobilelab.view.taskList.TaskListInterface

class TaskListPresenter(
    private var taskListView: TaskListInterface?,
    applicationContext: Context
) {

    private val context = taskListView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private lateinit var taskListAdapter: TaskListAdapter

    init {
        Repository.setAppDatabase(
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                Repository.appDatabaseName
            ).build()
        )
    }

    fun onDestroy() {
        taskListView = null
    }

    fun onFloatingActionButtonClick() {
        taskListView?.startActivityForResult(
            Intent(context, TaskEditActivity::class.java).also {
                it.putExtra(
                    TaskListActivity.REQUEST_CODE_STRING,
                    TaskListActivity.REQUEST_CODE_ADD_TASK
                )
            },
            TaskListActivity.REQUEST_CODE_ADD_TASK
        )
    }

    fun onExitButtonClick() {
        sharedPreferencesHandler.saveString(
            context.getString(R.string.shared_preferences_user_token),
            context.getString(R.string.shared_preferences_null_token)
        )

        taskListView?.finish()
    }

    fun initData() {
        val token = sharedPreferencesHandler.readString(
            context.getString(R.string.shared_preferences_user_token)
        )

        Repository.getTasks(
            token,
            {}
        ) {
            taskListView?.initRecyclerView()
        }
    }

    fun onItemClick(
        position: Int
    ) {
        val taskPosition = TaskListAdapter.getTaskPosition(position)

        taskListView?.startActivityForResult(
            Intent(context, TaskDetailsActivity::class.java).also {
                it.putExtra(TaskListActivity.TASK_ID, taskPosition)
            },
            TaskListActivity.REQUEST_CODE_EDIT_TASK
        )
    }

    fun onDoneClick(
        position: Int,
        setIsCheck: (Int) -> Unit
    ) {
        val taskPosition = TaskListAdapter.getTaskPosition(position)
        val taskData = Repository.getTasksData()[taskPosition]
        val token = sharedPreferencesHandler.readString(context.getString(R.string.shared_preferences_user_token))

        val done = when(taskData.done) {
            0 -> 1
            1 -> 0
            else -> -1
        }

        Repository.patchTask(
            token,
            taskData.id,
            TaskForm(
                taskData.title,
                taskData.description,
                done,
                taskData.deadline,
                taskData.category!!.id,
                taskData.priority!!.id
            ),
            { _, _ ->
                setIsCheck(Repository.getTasksData()[taskPosition].done)
            }
        ) { _, _ ->
            setIsCheck(Repository.getTasksData()[taskPosition].done)
        }

    }

    fun setTaskListAdapter(
        taskListAdapter: TaskListAdapter
    ) {
        this.taskListAdapter = taskListAdapter
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