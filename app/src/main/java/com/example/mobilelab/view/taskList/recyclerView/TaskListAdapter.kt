package com.example.mobilelab.view.taskList.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilelab.R
import com.example.mobilelab.model.Data
import com.example.mobilelab.model.DataList
import kotlinx.android.synthetic.main.task_view_category.view.*
import kotlinx.android.synthetic.main.task_view_task.view.*

class TaskListAdapter(
    val context: Context,
    private val dataList: DataList,
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
                    .inflate(R.layout.task_view_null, parent, false)
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
                holder.name.text = dataList.dataList[position].category
            }
            is TaskViewHolder -> {
                holder.apply {
                    color.background = dataList.dataList[position].color!!
                    name.text = dataList.dataList[position].name!!
                    beginning.text = dataList.dataList[position].description!!
                    done.isChecked = dataList.dataList[position].state!!
                }
            }
        }
    }

    override fun getItemCount() = dataList.dataList.size

    override fun getItemViewType(
        position: Int
    ): Int {
        return when (dataList.dataList[position].isTask) {
            false -> 0
            true -> 1
        }
    }

    fun deleteData(
        position: Int
    ) {
        dataList.deleteData(position)
        listener.onListChange(dataList.dataList.size)

        notifyDataSetChanged()
    }

    fun addData(
        data: Data
    ) {
        dataList.addData(data)
        listener.onListChange(dataList.dataList.size)

        notifyDataSetChanged()
    }

}