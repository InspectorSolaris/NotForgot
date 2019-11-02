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
        onDateSetPiced: (DatePicker?, Int, Int, Int) -> Unit
    )

    fun initScreenName(
        name: String
    )

    fun initTask(
        name: String,
        description: String,
        deadline: Int
    )

    fun initCategories(
        categoriesString: ArrayList<String>,
        categoryName: String
    )

    fun initPriorities(
        prioritiesString: ArrayList<String>,
        priorityName: String
    )

    fun finishActivity()

}