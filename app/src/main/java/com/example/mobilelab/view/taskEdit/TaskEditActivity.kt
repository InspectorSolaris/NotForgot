package com.example.mobilelab.view.taskEdit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilelab.R
import com.example.mobilelab.presenter.taskEdit.TaskEditPresenter
import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.content_task_edit.*
import kotlinx.android.synthetic.main.content_task_edit.taskCategory
import kotlinx.android.synthetic.main.content_task_edit.taskPriority
import java.util.*
import kotlin.collections.ArrayList

class TaskEditActivity :
    AppCompatActivity(),
    TaskEditInterface {

    private lateinit var taskEditPresenter: TaskEditPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)

        taskEditPresenter = TaskEditPresenter(this)

        addCategory.setOnClickListener {
            taskEditPresenter.onAddCategoryClick()
        }

        save.setOnClickListener {
            taskEditPresenter.onSaveClick(
                taskTitleEditText.text.toString(),
                taskDescriptionEditText.text.toString(),
                taskCategory.selectedItem.toString(),
                taskPriority.selectedItem.toString(),
                taskDeadlineEditText.text.toString()
            )
        }

        taskDeadlineEditText.setOnClickListener {
            taskEditPresenter.onTaskDeadlineClick()
        }

        toolbar.setNavigationOnClickListener {
            taskEditPresenter.onNavigationClick()
        }

        taskTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                taskTitleEditText.error = null
            }
        })

        taskDescriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                taskDescriptionEditText.error = null
            }
        })

        taskDeadlineEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                taskDeadlineEditText.error = null
            }
        })

        taskEditPresenter.initData(intent.extras)
    }

    companion object {
        const val TASK_TITLE = "taskName"
        const val TASK_DESCRIPTION = "taskDescription"
        const val TASK_CATEGORY_NAME = "taskCategory"
        const val TASK_PRIORITY_NAME = "taskPriority"
        const val TASK_DEADLINE = "taskDeadline"
        const val TASK_EDITED = "taskEdited"
    }

    override fun onDestroy() {
        taskEditPresenter.onDestroy()

        super.onDestroy()
    }

    override fun onSaveButtonClick(
        onPositiveButtonClick: () -> Unit,
        onNeutralButtonClick: () -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(this.getText(R.string.edit_alert_dialog_save_title))
            .setPositiveButton(this.getText(R.string.edit_alert_dialog_save_positive_button)) { _, _ ->
                onPositiveButtonClick()
            }
            .setNeutralButton(this.getText(R.string.edit_alert_dialog_save_neutral_button)) { _, _ ->
                onNeutralButtonClick()
            }
            .create()
            .show()
    }

    override fun addCategoryAlertDialog(
        onPositiveButtonClick: (DialogInterface, Int, String) -> Unit,
        onNegativeButtonClick: (DialogInterface, Int) -> Unit
    ) {
        AlertDialog.Builder(this)
            .setTitle(R.string.edit_alert_dialog_title)
            .setPositiveButton(R.string.edit_alert_dialog_positive_button) { dialogInterface, i ->
                val dialog = dialogInterface as Dialog
                val categoryName = (dialog.findViewById(R.id.addCategoryEditText) as EditText).text.toString()

                onPositiveButtonClick(dialogInterface, i, categoryName)
            }
            .setNegativeButton(R.string.edit_alert_dialog_negative_button) { dialogInterface, i ->
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

    override fun setScreenTitle(
        title: String
    ) {
        screenName.text = title
    }

    override fun setTaskTitle(
        title: String
    ) {
        taskTitleEditText.setText(title)
    }

    override fun setTaskDescription(
        description: String
    ) {
        taskDescriptionEditText.setText(description)
    }

    override fun setTaskDeadline(
        deadline: String
    ) {
        taskDeadlineEditText.setText(deadline)
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

    override fun setTitleError(
        errorText: String
    ) {
        taskTitleEditText.error = errorText
    }

    override fun setDescriptionError(
        errorText: String
    ) {
        taskDescriptionEditText.error = errorText
    }

    override fun setDeadlineError(
        errorText: String
    ) {
        taskDeadlineEditText.error = errorText
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun finishActivityWithResult(
        intent: Intent
    ) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
