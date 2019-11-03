package com.example.mobilelab.presenter.taskEdit

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.form.CategoryForm
import com.example.mobilelab.model.server.form.TaskForm
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.model.taskData.Task
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

    fun onSaveClick(
        title: String,
        description: String,
        categoryName: String,
        priorityName: String,
        deadline: String
    ) {
        taskEditView?.onSaveButtonClick({
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
                    val taskWasEdited = taskEdited(
                        title,
                        description,
                        categoryName,
                        priorityName,
                        deadline
                    )
                    var result = Activity.RESULT_CANCELED

                    if(taskWasEdited) {
                        val token = sharedPreferencesHandler.readString(context.getString(R.string.shared_preferences_user_token))
                        val deadlineLong = SimpleDateFormat(context.getString(R.string.date_pattern), Locale.US).parse(deadline)!!.time
                        val form = TaskForm(
                            title,
                            description,
                            0,
                            deadlineLong,
                            Repository.getCategoriesData().find { it.name == categoryName }!!.id,
                            Repository.getPrioritiesData().find { it.name == priorityName }!!.id
                        )

                        when(requestCode) {
                            TaskListActivity.REQUEST_CODE_ADD_TASK -> {
                                Repository.postTask(
                                    token,
                                    form,
                                    { _, _ ->
                                    }
                                ) { _, response ->
                                    if(response.body() != null) {
                                        result = Activity.RESULT_OK

                                        taskData = response.body()!!
                                    }
                                }
                            }
                            TaskListActivity.REQUEST_CODE_EDIT_TASK -> {
                                Repository.patchTask(
                                    token,
                                    taskData.id,
                                    form,
                                    { _, _ ->
                                    }
                                ) { _, response ->
                                    if(response.body() != null) {
                                        result = Activity.RESULT_OK
                                    }
                                }
                            }
                        }
                    }

                    taskEditView?.finishActivityWithResult(
                        result,
                        null
                    )
                }
            }, {}
        )
    }

    fun onAddCategoryClick() {
        taskEditView?.addCategoryAlertDialog(
            { _, _, categoryName ->
                if(categoryName.isNotEmpty()) {
                    Repository.postCategory(
                        sharedPreferencesHandler.readString(context.getString(R.string.shared_preferences_user_token)),
                        CategoryForm(categoryName),
                        { _, _ ->
                        }
                    ) { _, response ->
                        if(response.body() != null) {
                            initCategories()
                        }
                    }
                }
            },
            { _, _ ->

            }
        )
    }

    fun onTaskDeadlineClick() {
        taskEditView?.taskDeadlineDatePickerDialog { _, i, i2, i3 ->
            val calendar = Calendar.getInstance()

            calendar.set(i, i2, i3)

            val dateStr = SimpleDateFormat(context.getString(R.string.date_pattern), Locale.US).format(calendar.time)

            taskEditView?.setTaskDeadline(dateStr)
        }
    }

    fun onNavigationClick() {
        taskEditView?.finishActivityWithResult(
            Activity.RESULT_CANCELED,
            null
        )
    }

    fun initData(
        extras: Bundle?
    ) {
        requestCode = extras?.getInt(TaskListActivity.REQUEST_CODE_STRING)!!

        if(requestCode == TaskListActivity.REQUEST_CODE_ADD_TASK) {
            onRequestAddTask()
        } else if(requestCode == TaskListActivity.REQUEST_CODE_EDIT_TASK) {
            onRequestEditTask(extras)
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
            dateStr = SimpleDateFormat(context.getString(R.string.date_pattern), Locale.US).format(date)
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

    private fun onRequestAddTask() {
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
    }

    private fun onRequestEditTask(
        extras: Bundle?
    ) {
        val taskId = extras?.getInt(TaskListActivity.TASK_ID, -1)

        if(taskId != -1) {
            taskData = Repository.getTasksData()[taskId!!]
        }
    }

}