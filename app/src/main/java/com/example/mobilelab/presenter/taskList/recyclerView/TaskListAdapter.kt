package com.example.mobilelab.presenter.taskList.recyclerView

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilelab.R
import com.example.mobilelab.model.taskData.Task
import kotlinx.android.synthetic.main.task_view_category.view.*
import kotlinx.android.synthetic.main.task_view_task.view.*
import kotlin.random.Random

class TaskListAdapter(
    val context: Context,
    private val taskList: ArrayList<Task>,
    private val listener: Listener
) : RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    private val random = Random(System.currentTimeMillis())
    private val colors = arrayListOf(
        Color.BLUE, Color.CYAN, Color.GREEN,
        Color.MAGENTA, Color.RED, Color.YELLOW, Color.LTGRAY
    )

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

        var title: TextView = category.taskCategory

    }

    class TaskViewHolder(
        task: View
    ) : TaskListViewHolder(task) {

        var color: Drawable = task.taskColor.background
        var title: TextView = task.taskTitle
        var description: TextView = task.taskBeginning
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
                holder.title.text = taskList[position].category.name
            }
            is TaskViewHolder -> {
                holder.apply {
                    color = ColorDrawable(colors[random.nextInt(colors.size)])
                    title.text = taskList[position].title
                    description.text = taskList[position].description
                    done.isChecked = taskList[position].done == 1
                }
            }
        }

        listener.onListChange(taskList.size)
    }

    override fun getItemCount() = taskList.size + setOf(taskList.map { task -> task.category }).size

    override fun getItemViewType(
        position: Int
    ): Int {
        val categoriesAmount = setOf(taskList.subList(0, position).map { task -> task.category}).size

        return if (position == 0 || position > 1 && taskList[position - 1 - categoriesAmount].category != taskList[position - categoriesAmount].category) {
            0
        } else {
            1
        }
    }

    fun deleteData(
        position: Int
    ) {
        taskList.removeAt(position)
        listener.onListChange(taskList.size)

        notifyItemRemoved(position)
    }

    fun addData(
        newTask: Task
    ) {
        val position = taskList.indexOfLast { task -> task.category.id == newTask.category.id } + 1

        taskList.add(position, newTask)
        listener.onListChange(taskList.size)

        notifyItemInserted(position)
    }

}