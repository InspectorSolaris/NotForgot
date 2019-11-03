package com.example.mobilelab.view.taskEdit

import android.content.DialogInterface
import android.content.Intent
import android.widget.DatePicker
import kotlin.collections.ArrayList

interface TaskEditInterface {

    fun onSaveButtonClick(
        onPositiveButtonClick: () -> Unit,
        onNeutralButtonClick: () -> Unit
    )

    fun addCategoryAlertDialog(
        onPositiveButtonClick: (DialogInterface, Int, String) -> Unit,
        onNegativeButtonClick: (DialogInterface, Int) -> Unit
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

    fun finishActivity(
        resultCode: Int,
        intent: Intent?
    )

}