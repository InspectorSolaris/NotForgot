package com.example.mobilelab.view.taskList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilelab.R
import com.example.mobilelab.presenter.taskList.TaskListPresenter
import com.example.mobilelab.view.taskEdit.TaskEditActivity
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
        const val TASK_ID = "taskId"                    // for pass task id
        const val REQUEST_CODE_STRING = "requestCode"   // for pass request code
        const val REQUEST_CODE_ADD_TASK = 1             // for go in edit activity
        const val REQUEST_CODE_EDIT_TASK = 2            // for go in details activity
    }

    override fun onDestroy() {
        taskListPresenter.onDestroy()

        super.onDestroy()
    }

    override fun onFloatingActionButtonClick() {
        val intent = Intent(this, TaskEditActivity::class.java)

        startActivityForResult(intent, REQUEST_CODE_ADD_TASK)
    }

    override fun initRecyclerView() {
        val context = this
        val adapter = TaskListAdapter(
            this,
            object : TaskListAdapter.Listener {

                override fun onListChange(size: Int) {
                    if (size == 0) {
                        noTasks.visibility = View.VISIBLE
                    } else {
                        noTasks.visibility = View.GONE
                    }
                }

            }
        )
        ItemTouchHelper(
            TaskListSwipeToDeleteSimpleCallback(
                adapter
            )
        ).also {
            it.attachToRecyclerView(taskList)
        }

        taskListPresenter.setTaskListAdapter(adapter)

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
        taskListPresenter.startActivityForResult(intent, requestCode)

        super.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        taskListPresenter.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

}
