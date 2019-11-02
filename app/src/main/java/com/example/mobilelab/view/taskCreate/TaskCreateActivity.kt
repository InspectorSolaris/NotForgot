package com.example.mobilelab.view.taskCreate

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.mobilelab.R
import com.example.mobilelab.presenter.taskCreate.TaskCreatePresenter
import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.add_category_view.view.*
import kotlinx.android.synthetic.main.content_task_create.*
import kotlinx.android.synthetic.main.content_task_create.taskCategory
import kotlinx.android.synthetic.main.content_task_create.taskPriority
import kotlinx.android.synthetic.main.content_task_create.taskTitle
import java.text.SimpleDateFormat
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
            taskCreatePresenter.onSaveClick()
        }

        taskDeadline.setOnClickListener {
            taskCreatePresenter.onTaskDeadlineClick()
        }

        toolbar.setNavigationOnClickListener {
            taskCreatePresenter.onNavigationClick()
        }

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
            .create().show()
    }

    override fun taskDeadlineDatePickerDialog(
        onDateSetPiced: (DatePicker?, Int, Int, Int) -> Unit
    ) {
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 -> onDateSetPiced(p0, p1, p2, p3) },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun initScreenName(
        name: String
    ) {
        screenName.text = name
    }

    override fun initTask(
        name: String,
        description: String,
        deadline: Int
    ) {
        var deadlineStr = ""
        if(deadline != -1) {
            deadlineStr = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(Date(deadline.toLong()))
        }

        taskTitle.setText(name)
        taskDescriptionEditText.setText(description)
        taskDeadline.setText(deadlineStr)
    }

    override fun initCategories(
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

    override fun initPriorities(
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
