package com.example.mobilelab.view.taskDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.presenter.taskDetails.TaskDetailsPresenter
import com.example.mobilelab.view.taskEdit.TaskEditActivity
import com.example.mobilelab.view.taskList.TaskListActivity
import kotlinx.android.synthetic.main.content_task_details.*

class TaskDetailsActivity
    : AppCompatActivity(),
    TaskDetailsInterface {

    private lateinit var taskDetailsPresenter: TaskDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        taskDetailsPresenter = TaskDetailsPresenter(this)

        edit.setOnClickListener {
            taskDetailsPresenter.onEditClick()
        }

        taskDetailsPresenter.initDate(intent.extras)
    }

    override fun onDestroy() {
        taskDetailsPresenter.onDestroy()

        super.onDestroy()
    }

    override fun onEditButtonClick() {
        val intent = Intent(this, TaskEditActivity::class.java)

        startActivityForResult(intent, TaskListActivity.REQUEST_CODE_EDIT_TASK)
    }

    override fun setTaskTitle(
        title: String
    ) {
        taskTitle.text = title
    }

    override fun setTaskDescription(
        description: String
    ) {
        taskDescription.text = description
    }

    override fun setTaskState(
        state: String,
        color: Int
    ) {
        taskState.text = state
        taskState.setTextColor(color)
    }

    override fun setTaskCreated(
        created: String
    ) {
        taskCreated.text = created
    }

    override fun setTaskDeadline(
        deadline: String
    ) {
        taskDeadline.text = deadline
    }

    override fun setTaskCategory(
        category: String
    ) {
        taskCategory.text = category
    }

    override fun setTaskPriority(
        priority: String,
        color: Int
    ) {
        taskPriority.text = priority
        taskPriority.setBackgroundColor(color)
    }

    override fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        taskDetailsPresenter.startActivityForResult(intent, requestCode)

        super.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        taskDetailsPresenter.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun finishActivityWithResult(
        intent: Intent
    ) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
