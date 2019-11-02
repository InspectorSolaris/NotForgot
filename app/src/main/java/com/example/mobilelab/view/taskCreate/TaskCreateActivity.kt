package com.example.mobilelab.view.taskCreate

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.presenter.taskCreate.TaskCreatePresenter
import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.content_task_create.*
import kotlinx.android.synthetic.main.content_task_create.taskCategory
import kotlinx.android.synthetic.main.content_task_create.taskPriority
import kotlinx.android.synthetic.main.content_task_create.taskTitle
import java.util.*
import kotlin.collections.ArrayList

class TaskCreateActivity :
    AppCompatActivity(),
    TaskCreateInterface {

    private lateinit var taskCreatePresenter: TaskCreatePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create)

        taskCreatePresenter = TaskCreatePresenter(this)

        addCategory.setOnClickListener {
            taskCreatePresenter.onAddCategoryClick()
        }

        save.setOnClickListener {
            taskCreatePresenter.onSaveClick(
                taskTitle.text.toString(),
                taskDescriptionEditText.text.toString(),
                taskCategory.selectedItem.toString(),
                taskPriority.selectedItem.toString(),
                taskDeadline.text.toString()
            )
        }

        taskDeadline.setOnClickListener {
            taskCreatePresenter.onTaskDeadlineClick()
        }

        toolbar.setNavigationOnClickListener {
            taskCreatePresenter.onNavigationClick()
        }

        taskDescriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        taskCreatePresenter.initData(intent.extras)
    }

    override fun onDestroy() {
        taskCreatePresenter.onDestroy()

        super.onDestroy()
    }

    override fun addCategoryAlertDialog(
        onPositiveButtonClick: (DialogInterface, Int, String) -> Unit,
        onNegativeButtonClick: (DialogInterface, Int) -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(R.string.create_alert_dialog_title)
            .setPositiveButton(R.string.create_alert_dialog_positive_button) {dialogInterface, i ->
                val dialog = dialogInterface as Dialog
                val categoryName = (dialog.findViewById(R.id.addCategoryEditText) as EditText).text.toString()

                onPositiveButtonClick(dialogInterface, i, categoryName)
            }
            .setNegativeButton(R.string.create_alert_dialog_negative_button) {dialogInterface, i ->
                onNegativeButtonClick(dialogInterface, i)
            }
            .setView(R.layout.add_category_view)
            .create()
            .show()
    }

    override fun taskDeadlineDatePickerDialog(
        onDatePicked: (DatePicker?, Int, Int, Int) -> Unit
    ) {
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 -> onDatePicked(p0, p1, p2, p3) },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun setScreenName(
        name: String
    ) {
        screenName.text = name
    }

    override fun setTaskName(
        name: String
    ) {
        taskTitle.setText(name)
    }

    override fun setTaskDescription(
        description: String
    ) {
        taskDescriptionEditText.setText(description)
    }

    override fun setTaskDeadline(
        deadline: String
    ) {
        taskDeadline.setText(deadline)
    }

    override fun setCategories(
        categoriesString: ArrayList<String>,
        categoryName: String
    ) {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categoriesString
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            taskCategory.adapter = adapter

            if(categoryName.isNotEmpty()) {
                val position = adapter.getPosition(categoryName)

                taskCategory.setSelection(position)
            }
        }
    }

    override fun setPriorities(
        prioritiesString: ArrayList<String>,
        priorityName: String
    ) {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            prioritiesString
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            taskPriority.adapter = adapter

            if(priorityName.isNotEmpty()) {
                val position = adapter.getPosition(priorityName)

                taskPriority.setSelection(position)
            }
        }
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

}
