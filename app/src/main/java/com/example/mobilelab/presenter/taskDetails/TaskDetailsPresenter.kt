package com.example.mobilelab.presenter.taskDetails

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.view.taskDetails.TaskDetailsInterface
import com.example.mobilelab.view.taskEdit.TaskEditActivity
import com.example.mobilelab.view.taskList.TaskListActivity
import java.text.SimpleDateFormat
import java.util.*

class TaskDetailsPresenter(
    private var taskDetailsView: TaskDetailsInterface?
) {

    private val context = taskDetailsView as Context
    private var taskId = -1

    fun onDestroy() {
        taskDetailsView = null
    }

    fun onNavigationClick() {
        taskDetailsView?.onNavigationClick()
    }

    fun onEditClick() {
        taskDetailsView?.startActivityForResult(
            Intent(context, TaskEditActivity::class.java).also {
                it.putExtra(TaskListActivity.TASK_ID, taskId)
                it.putExtra(
                    TaskListActivity.REQUEST_CODE_STRING,
                    TaskListActivity.REQUEST_CODE_EDIT_TASK
                )
            },
            TaskListActivity.REQUEST_CODE_EDIT_TASK
        )
    }

    fun initDate(
        extras: Bundle?
    ) {
        taskId = extras?.getInt(TaskListActivity.TASK_ID, -1) ?: -1

        if (taskId == -1) {
            return
        }

        val task = Repository.getTasksData()[taskId]

        taskDetailsView?.setTaskTitle(task.title)
        taskDetailsView?.setTaskDescription(task.description)
        taskDetailsView?.setTaskState(getDoneAsString(task.done), getDoneAsColor(task.done))
        taskDetailsView?.setTaskCreated(getDateAsString(task.created))
        taskDetailsView?.setTaskDeadline(getDateAsString(task.deadline))
        taskDetailsView?.setTaskCategory(getCategoryAsString(task.category))
        taskDetailsView?.setTaskPriority(getPriorityAsString(task.priority), getPriorityAsColor(task.priority))
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        taskDetailsView?.finishActivityWithResult(
            resultCode,
            data
        )
    }

    private fun getDoneAsString(
        done: Int
    ): String {
        if(done == 1) {
            return context.getString(R.string.details_task_done)
        }

        return context.getString(R.string.details_task_not_done)
    }

    private fun getDoneAsColor(
        done: Int
    ): Int {
        if(done == 1) {
            return context.getColor(R.color.detailsTaskDone)
        }

        return context.getColor(R.color.detailsTaskNotDone)
    }

    private fun getDateAsString(
        date: Long
    ): String {
        var dateStr = ""
        val millsPerSec = 1000

        if(date != -1L) {
            dateStr = SimpleDateFormat(context.getString(R.string.date_pattern), Locale.US).format(date * millsPerSec)
        }

        return dateStr
    }

    private fun getCategoryAsString(
        category: Category?
    ): String {
        if(category != null) {
            return category.name
        }

        return "NULL"
    }

    private fun getPriorityAsString(
        priority: Priority?
    ): String {
        if(priority != null) {
            return priority.name
        }

        return "NULL"
    }

    private fun getPriorityAsColor(
        priority: Priority?
    ): Int {
        if(priority != null) {
            return Color.parseColor(priority.color)
        }

        return 0
    }

}