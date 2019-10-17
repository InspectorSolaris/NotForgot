package com.example.mobilelab.view.taskList.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilelab.R
import com.example.mobilelab.model.TaskViewData
import kotlinx.android.synthetic.main.task_view_category.view.*
import kotlinx.android.synthetic.main.task_view_task.view.*

class TaskListAdapter(
    private val taskList: ArrayList<TaskViewData>
) : RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    open class TaskListViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)

    class TaskViewHolder(
        task: View
    ) : TaskListViewHolder(task) {

        var color: ConstraintLayout = task.taskColor
        var name: TextView = task.taskName
        var beginning: TextView = task.taskBeginning
        var done: CheckBox = task.taskDone

    }

    class CategoryViewHolder(
        category: View
    ) : TaskListViewHolder(category) {

        var name: TextView = category.categoryName
        
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListViewHolder {

        val view = when (viewType) {
            0 -> {
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.task_view_category, parent, false)
            }
            1 -> {
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.task_view_task, parent, false)
            }
            else -> {
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.task_view_category, parent, false)
            }
        }

        return TaskListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskListViewHolder,
        position: Int
    ) {
        when (holder) {
            is CategoryViewHolder -> {
                holder.name.text = taskList[position].category
            }
            is TaskViewHolder -> {
                holder.apply {
                    if (
                        taskList[position].color != null &&
                        taskList[position].name != null &&
                        taskList[position].description != null &&
                        taskList[position].state != null
                    ) {
                        color.background = taskList[position].color
                        name.text = taskList[position].name
                        beginning.text = taskList[position].description
                        done.isChecked = taskList[position].state!!
                    }
                }
            }
        }
    }

    override fun getItemCount() = taskList.size

    override fun getItemViewType(
        position: Int
    ): Int {
        return when (taskList[position].isTask) {
            false -> 0
            true -> 1
        }
    }

}