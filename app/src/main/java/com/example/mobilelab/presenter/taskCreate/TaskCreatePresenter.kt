package com.example.mobilelab.presenter.taskCreate

import android.content.Context
import android.os.Bundle
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.SharedPreferencesHandler
import com.example.mobilelab.model.server.form.CategoryForm
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.model.taskData.Task
import com.example.mobilelab.view.taskCreate.TaskCreateInterface
import com.example.mobilelab.view.taskList.TaskListActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskCreatePresenter(
    private var taskCreateView: TaskCreateInterface?
) {

    private val context = taskCreateView as Context
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private var requestCode: Int = 0
    private lateinit var taskData: Task

    fun onDestroy() {
        taskCreateView = null
    }

    fun onAddCategoryClick() {
        taskCreateView?.addCategoryAlertDialog(
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
        name: String,
        description: String,
        categoryName: String,
        priorityName: String,
        date: String
    ) {

    }

    fun onTaskDeadlineClick() {
        taskCreateView?.taskDeadlineDatePickerDialog { _, i, i2, i3 ->
            val calendar = Calendar.getInstance()

            calendar.set(i, i2, i3)

            taskCreateView?.setTaskDeadline(SimpleDateFormat("dd.MM.yyyy", Locale.US).format(calendar.time))
        }
    }

    fun onNavigationClick() {
        taskCreateView?.finishActivity()
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

            taskCreateView?.setScreenName(context.getString(R.string.create_screen_name_create))
        } else if(requestCode == TaskListActivity.REQUEST_CODE_EDIT_TASK) {

            taskData = extras.getParcelable(TaskListActivity.TASK_DATA)!!
            taskCreateView?.setScreenName(context.getString(R.string.create_screen_name_edit))

        }

        taskCreateView?.setTaskName(taskData.title)
        taskCreateView?.setTaskDescription(taskData.description)
        taskCreateView?.setTaskDeadline(getDateAsString(taskData.deadline))

        initCategories()
        initPriorities()
    }

    private fun initCategories() {
        taskCreateView?.setCategories(ArrayList(Repository.getCategoriesData().map { it.name }), taskData.category!!.name)
    }

    private fun initPriorities() {
        taskCreateView?.setPriorities(ArrayList(Repository.getPrioritiesData().map { it.name }), taskData.priority!!.name)
    }

    private fun getDateAsString(
        time: Long
    ): String {
        var date = ""

        if(time != -1L) {
            date = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(time)
        }

        return date
    }

}