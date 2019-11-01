package com.example.mobilelab.view.taskList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilelab.R
import com.example.mobilelab.model.taskData.Task
import com.example.mobilelab.presenter.taskList.TaskListPresenter
import com.example.mobilelab.view.taskCreate.TaskCreateActivity
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListAdapter
import com.example.mobilelab.presenter.taskList.recyclerView.TaskListSwipeToDeleteSimpleCallback

import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.content_task_list.*

class TaskListActivity :
    AppCompatActivity(),
    TaskListInterface {

    private lateinit var taskListPresenter: TaskListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(toolbar)

        taskListPresenter = TaskListPresenter(this)

        fab.setOnClickListener {
            taskListPresenter.onFloatingActionButtonClick()
        }

        exit.setOnClickListener {
            taskListPresenter.onExitButtonClick()
        }

        taskListPresenter.initData()
    }

    companion object {
        const val REQUEST_CODE_STRING = "requestCode"
        const val REQUEST_CODE_ADD_TASK = 1
        const val REQUEST_CODE_EDIT_TASK = 1
        const val RESULT_CODE_ADD_TASK = 1
    }

    override fun onDestroy() {
        taskListPresenter.onDestroy()

        super.onDestroy()
    }

    override fun onFloatingActionButtonClick() {
        val intent = Intent(this, TaskCreateActivity::class.java)

        startActivityForResult(intent, REQUEST_CODE_ADD_TASK)
    }

    override fun initRecyclerView(
        taskDataList: ArrayList<Task>
    ) {
        val context = this
        val adapter = TaskListAdapter(
            this,
            taskDataList,
            object :
                TaskListAdapter.Listener {

                override fun onListChange(
                    size: Int
                ) {
                    if (size == 0) {
                        noTasks.visibility = View.VISIBLE
                    } else {
                        noTasks.visibility = View.GONE
                    }
                }

            }
        )
        val itemTouchHelper = ItemTouchHelper(
            TaskListSwipeToDeleteSimpleCallback(
                adapter
            )
        ).also {
            it.attachToRecyclerView(taskList)
        }

        taskListPresenter.setTaskListAdapter(adapter)
        taskListPresenter.setItemTouchHelper(itemTouchHelper)

        taskList.also {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    override fun finishActivity() {
        finish()
    }

    override fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        intent?.putExtra(REQUEST_CODE_STRING, requestCode)
        super.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_ADD_TASK &&
                resultCode == Activity.RESULT_OK) {

            data?.extras

        }
    }

}
