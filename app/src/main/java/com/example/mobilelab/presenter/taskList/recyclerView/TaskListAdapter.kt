package com.example.mobilelab.presenter.taskList.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilelab.R
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.data.addDataToList
import com.example.mobilelab.model.data.deleteDataToList
import kotlinx.android.synthetic.main.task_view_category.view.*
import kotlinx.android.synthetic.main.task_view_task.view.*

class TaskListAdapter(
    val context: Context,
    private val taskDataList: ArrayList<TaskData>,
    private val listener: Listener
) : RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    interface Listener {

        fun onListChange(
            size: Int
        )

    }

    open class TaskListViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)

    class CategoryViewHolder(
        category: View
    ) : TaskListViewHolder(category) {

        var name: TextView = category.nullName

    }

    class TaskViewHolder(
        task: View
    ) : TaskListViewHolder(task) {

        var color: ConstraintLayout = task.taskColor
        var name: TextView = task.taskName
        var beginning: TextView = task.taskBeginning
        var done: CheckBox = task.taskDone

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListViewHolder {

        when (viewType) {
            0 -> {
                return TaskViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.task_view_category, parent, false)
                )
            }
            1 -> {
                return CategoryViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.task_view_task, parent, false)
                )
            }
            else -> {
                return TaskListViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.task_view_null, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(
        holder: TaskListViewHolder,
        position: Int
    ) {
        when (holder) {
            is CategoryViewHolder -> {
                holder.name.text = taskDataList[position].category
            }
            is TaskViewHolder -> {
                holder.apply {
                    color.background = taskDataList[position].color!!
                    name.text = taskDataList[position].name!!
                    beginning.text = taskDataList[position].description!!
                    done.isChecked = taskDataList[position].state!!
                }
            }
        }

        listener.onListChange(taskDataList.size)
    }

    override fun getItemCount() = taskDataList.size

    override fun getItemViewType(
        position: Int
    ): Int {
        return when (taskDataList[position].isTask) {
            false -> 0
            true -> 1
        }
    }

    fun deleteData(
        position: Int
    ) {
        deleteDataToList(position, taskDataList)
        listener.onListChange(taskDataList.size)

        notifyDataSetChanged()
    }

    fun addData(
        taskData: TaskData
    ) {
        addDataToList(taskData, taskDataList)
        listener.onListChange(taskDataList.size)

        notifyDataSetChanged()
    }

}