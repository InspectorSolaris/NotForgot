package com.example.mobilelab.view.taskCreate

import android.content.DialogInterface
import android.widget.DatePicker
import kotlin.collections.ArrayList

interface TaskCreateInterface {

    fun addCategoryAlertDialog(
        onPositiveButtonClick: (DialogInterface, Int, String) -> Unit,
        onNegativeButtonClick: (DialogInterface, Int) -> Unit
    )

    fun taskDeadlineDatePickerDialog(
        onDatePicked: (DatePicker?, Int, Int, Int) -> Unit
    )

    fun setScreenName(
        name: String
    )

    fun setTaskName(
        name: String
    )

    fun setTaskDescription(
        description: String
    )

    fun setTaskDeadline(
        deadline: String
    )

    fun setCategories(
        categoriesString: ArrayList<String>,
        categoryName: String
    )

    fun setPriorities(
        prioritiesString: ArrayList<String>,
        priorityName: String
    )

    fun finishActivity()

}