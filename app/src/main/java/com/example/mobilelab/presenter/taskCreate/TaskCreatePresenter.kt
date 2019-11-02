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
import kotlin.collections.ArrayList

class TaskCreatePresenter(
    private var taskCreateView: TaskCreateInterface?
) {

    private val context = taskCreateView as Context
    private val repository = Repository()
    private val sharedPreferencesHandler = SharedPreferencesHandler(
        context,
        context.getString(R.string.shared_preferences_file)
    )
    private var requestCode: Int = 0
    private lateinit var categoriesData: ArrayList<Category>
    private lateinit var prioritiesData: ArrayList<Priority>
    private lateinit var taskData: Task

    fun onDestroy() {
        taskCreateView = null
    }

    fun onAddCategoryClick() {
        taskCreateView?.addCategoryAlertDialog(
            { _, _, categoryName ->

                if(categoryName.isNotEmpty()) {

                    repository.postCategory(
                        sharedPreferencesHandler.readString(context.getString(R.string.shared_preferences_user_token)),
                        CategoryForm(
                            categoryName
                        ),
                        { _, _ ->

                        },
                        { _, response ->
                            if(response.body() != null) {
                                categoriesData.add(response.body()!!)

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

    fun onSaveClick() {

    }

    fun onTaskDeadlineClick() {
        taskCreateView?.taskDeadlineDatePickerDialog({ _, i, i2, i3 ->

        })
    }

    fun onNavigationClick() {
        taskCreateView?.finishActivity()
    }

    fun initData(
        extras: Bundle?
    ) {
        requestCode = extras?.getInt(TaskListActivity.REQUEST_CODE_STRING)!!
        categoriesData = extras.getParcelableArrayList(TaskListActivity.TASK_CATEGORIES)!!
        prioritiesData = extras.getParcelableArrayList(TaskListActivity.TASK_PRIORITIES)!!

        if(requestCode == TaskListActivity.REQUEST_CODE_ADD_TASK) {
            taskData = Task(
                -1,
                "",
                "",
                0,
                0,
                0,
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

            taskCreateView?.initScreenName(context.getString(R.string.create_screen_name_create))
        } else if(requestCode == TaskListActivity.REQUEST_CODE_EDIT_TASK) {

            taskData = extras.getParcelable(TaskListActivity.TASK_DATA)!!
            taskCreateView?.initScreenName(context.getString(R.string.create_screen_name_edit))

        }

        taskCreateView?.initTask(
            taskData.title,
            taskData.description,
            taskData.deadline
        )

        initCategories()
        initPriorities()
    }

    private fun initCategories() {
        taskCreateView?.initCategories(ArrayList(categoriesData.map { it.name }), taskData.category!!.name)
    }

    private fun initPriorities() {
        taskCreateView?.initPriorities(ArrayList(prioritiesData.map { it.name }), taskData.priority!!.name)
    }

}