package com.example.mobilelab.presenter.taskEdit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.form.CategoryForm
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.model.taskData.Task
import com.example.mobilelab.view.taskEdit.TaskEditActivity
import com.example.mobilelab.view.taskEdit.TaskEditInterface
import com.example.mobilelab.view.taskList.TaskListActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskEditPresenter(
    private var taskEditView: TaskEditInterface?
) {

    private val context = taskEditView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private var requestCode: Int = 0
    private lateinit var taskData: Task

    fun onDestroy() {
        taskEditView = null
    }

    fun onAddCategoryClick() {
        taskEditView?.addCategoryAlertDialog(
            { _, _, categoryName ->

                if(categoryName.isNotEmpty()) {

                    Repository.postCategory(
                        sharedPreferencesHandler.readString(context.getString(R.string.shared_preferences_user_token)),
                        CategoryForm(
                            categoryName
                        ),
                        { _, _ ->
                        },
                        { _, response ->
                            if(response.body() != null) {
                                Repository.getCategoriesData().add(response.body()!!)

                                initCategories()
                            }
                        }
                    )

                }
            },
            { _, _ ->

            }
        )
    }

    fun onSaveClick(
        title: String,
        description: String,
        categoryName: String,
        priorityName: String,
        deadline: String
    ) {
        var noErrors = true

        if(title.isEmpty()) {
            taskEditView?.setTitleError(context.getString(R.string.edit_task_title_is_empty))

            noErrors = false
        }

        if(description.isEmpty()) {
            taskEditView?.setDescriptionError(context.getString(R.string.edit_task_description_is_empty))

            noErrors = false
        }

        if(description.length > 120) {
            taskEditView?.setDescriptionError(context.getString(R.string.edit_task_description_too_long))

            noErrors = false
        }

        if(deadline.isEmpty()) {
            taskEditView?.setDeadlineError(context.getString(R.string.edit_task_deadline_is_empty))

            noErrors = false
        }

        if(noErrors) {
            taskEditView?.onSaveButtonClick({
                taskEditView?.finishActivityWithResult(
                    Intent().also {
                        it.putExtra(TaskEditActivity.TASK_TITLE, title)
                        it.putExtra(TaskEditActivity.TASK_DESCRIPTION, description)
                        it.putExtra(TaskEditActivity.TASK_CATEGORY_NAME, categoryName)
                        it.putExtra(TaskEditActivity.TASK_PRIORITY_NAME, priorityName)
                        it.putExtra(TaskEditActivity.TASK_DEADLINE, deadline)
                        it.putExtra(TaskEditActivity.TASK_EDITED, taskEdited(
                            title,
                            description,
                            categoryName,
                            priorityName,
                            deadline
                        ))
                    }
                )
            }, {})
        }
    }

    fun onTaskDeadlineClick() {
        taskEditView?.taskDeadlineDatePickerDialog { _, i, i2, i3 ->
            val calendar = Calendar.getInstance()

            calendar.set(i, i2, i3)

            val dateStr = SimpleDateFormat(context.getString(R.string.edit_task_date_pattern), Locale.US).format(calendar.time)

            taskEditView?.setTaskDeadline(dateStr)
        }
    }

    fun onNavigationClick() {
        taskEditView?.finishActivity()
    }

    fun initData(
        extras: Bundle?
    ) {
        requestCode = extras?.getInt(TaskListActivity.REQUEST_CODE_STRING)!!

        if(requestCode == TaskListActivity.REQUEST_CODE_ADD_TASK) {
            taskData = Task(
                -1,
                "",
                "",
                -1,
                -1,
                -1,
                Category(
                    -1,
                    ""
                ),
                Priority(
                    -1,
                    "",
                    ""
                )
            )
            taskEditView?.setScreenTitle(context.getString(R.string.edit_screen_title_create))
        } else if(requestCode == TaskListActivity.REQUEST_CODE_EDIT_TASK) {
            val taskId = extras.getInt(TaskListActivity.TASK_ID, -1)

            if(taskId != -1) {
                val localTask = Repository.getTasksData().find { it.id == taskId }

                if(localTask != null) {
                    taskData = localTask
                    taskEditView?.setScreenTitle(context.getString(R.string.edit_screen_title_edit))
                }
            }
        }

        taskEditView?.setTaskTitle(taskData.title)
        taskEditView?.setTaskDescription(taskData.description)
        taskEditView?.setTaskDeadline(getDateAsString(taskData.deadline))

        initCategories()
        initPriorities()
    }

    private fun initCategories() {
        taskEditView?.setCategories(ArrayList(Repository.getCategoriesData().map { it.name }), taskData.category!!.name)
    }

    private fun initPriorities() {
        taskEditView?.setPriorities(ArrayList(Repository.getPrioritiesData().map { it.name }), taskData.priority!!.name)
    }

    private fun getDateAsString(
        date: Long
    ): String {
        var dateStr = ""

        if(date != -1L) {
            dateStr = SimpleDateFormat(context.getString(R.string.edit_task_date_pattern), Locale.US).format(date)
        }

        return dateStr
    }

    private fun taskEdited(
        title: String,
        description: String,
        categoryName: String,
        priorityName: String,
        deadline: String
    ): Boolean {
        return taskData.title != title ||
                taskData.description != description ||
                taskData.category!!.name != categoryName ||
                taskData.priority!!.name != priorityName ||
                getDateAsString(taskData.deadline) != deadline
    }

}