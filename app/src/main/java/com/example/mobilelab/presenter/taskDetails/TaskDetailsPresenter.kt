package com.example.mobilelab.presenter.taskDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.view.taskDetails.TaskDetailsInterface
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

    fun onEditClick() {
        taskDetailsView?.onEditButtonClick()
    }

    fun initDate(
        extras: Bundle?
    ) {
        val taskId = extras?.getInt(TaskListActivity.TASK_ID, -1)

        if (taskId == -1) {
            return
        }

        val task = Repository.getTasksData().find { it.id == taskId }

        if (task != null) {
            taskDetailsView?.setTaskTitle(task.title)
            taskDetailsView?.setTaskDescription(task.description)
            taskDetailsView?.setTaskState(getDoneAsString(task.done), getDoneAsColor(task.done))
            taskDetailsView?.setTaskCreated(getDateAsString(task.created))
            taskDetailsView?.setTaskDeadline(getDateAsString(task.deadline))
            taskDetailsView?.setTaskCategory(getCategoryAsString(task.category))
            taskDetailsView?.setTaskPriority(getPriorityAsString(task.priority), getPriorityAsColor(task.priority))
        }
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

        if(date != -1L) {
            dateStr = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(date)
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
            return priority.color.substring(1, 7).toInt()
        }

        return 0
    }

    fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        intent?.putExtra(TaskListActivity.TASK_ID, taskId)
        intent?.putExtra(TaskListActivity.REQUEST_CODE_STRING, requestCode)
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

    }

}