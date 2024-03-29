package com.example.mobilelab.view.taskEdit

import android.content.Intent
import android.widget.DatePicker
import kotlin.collections.ArrayList

interface TaskEditInterface {

    fun onSaveButtonClick(
        onPositiveButtonClick: () -> Unit
    )

    fun addCategoryAlertDialog(
        onPositiveButtonClick: (String) -> Unit
    )

    fun taskDeadlineDatePickerDialog(
        onDatePicked: (DatePicker?, Int, Int, Int) -> Unit
    )

    fun setScreenTitle(
        title: String
    )

    fun setTaskTitle(
        title: String
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

    fun setTitleError(
        errorText: String
    )

    fun setDescriptionError(
        errorText: String
    )

    fun setDeadlineError(
        errorText: String
    )

    fun finishActivityWithResult(
        resultCode: Int,
        intent: Intent?
    )

}