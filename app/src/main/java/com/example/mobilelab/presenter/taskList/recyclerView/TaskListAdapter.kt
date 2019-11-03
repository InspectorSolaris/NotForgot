package com.example.mobilelab.presenter.taskList.recyclerView

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilelab.R
import com.example.mobilelab.model.Repository
import com.example.mobilelab.model.taskData.Task
import kotlinx.android.synthetic.main.task_view_category.view.*
import kotlinx.android.synthetic.main.task_view_task.view.*
import kotlin.math.max
import kotlin.random.Random

class TaskListAdapter(
    val context: Context,
    private val listener: Listener,
    private val tasksData: ArrayList<Task> = Repository.getTasksData()
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

        var color: ConstraintLayout = task.taskColor
        var title: TextView = task.taskTitle
        var description: TextView = task.taskCreated
        var done: CheckBox = task.taskDone

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListViewHolder {
        when (viewType) {
            0 -> {
                return CategoryViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.task_view_category, parent, false)
                )
            }
            1 -> {
                return TaskViewHolder(
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
        val taskPos = getTaskPosition(position)
        val categoryPos = getCategoryPosition(position)

        when (holder) {
            is CategoryViewHolder -> {
                holder.title.text = tasksData[categoryPos].category?.name
            }
            is TaskViewHolder -> {
                holder.apply {
                    color.setBackgroundColor(colors[random.nextInt(colors.size)])
                    title.text = tasksData[taskPos].title
                    description.text = tasksData[taskPos].description
                    done.isChecked = tasksData[taskPos].done == 1
                }
            }
        }

        listener.onListChange(tasksData.size)
    }

    private fun getAmountOfCategories(
        position: Int
    ): Int {
        var amountOfTasks = 0
        var amountOfCategories = 0
        while(amountOfCategories + amountOfTasks + 1 + HashSet(tasksData.filter { task -> task.category == tasksData[amountOfTasks].category }).size < position) {
            amountOfTasks += HashSet(tasksData.filter { task -> task.category == tasksData[amountOfTasks].category }).size
            ++amountOfCategories
        }

        return amountOfCategories + 1
    }

    private fun getCategoryPosition(
        position: Int
    ): Int {
        return max(getTaskPosition(position), 0)
    }

    private fun getTaskPosition(
        position: Int
    ): Int {
        return position - getAmountOfCategories(position)
    }

    override fun getItemCount(): Int {
        return tasksData.size + HashSet(tasksData.map { task -> task.category }).size
    }

    override fun getItemViewType(
        position: Int
    ): Int {
        val prevTaskPos = getTaskPosition(position - 1)
        val curTaskPos = getTaskPosition(position)
        val itemIsCategory = position == 0 || position > 1 && tasksData[prevTaskPos].category != tasksData[curTaskPos].category

        return if (itemIsCategory) {
            0
        } else {
            1
        }
    }

    fun deleteData(
        position: Int
    ) {
        tasksData.removeAt(getTaskPosition(position))
        listener.onListChange(tasksData.size)

        notifyDataSetChanged()
    }

    fun addData(
        newTask: Task
    ) {
        val position = tasksData.indexOfLast { task -> task.category?.id == newTask.category?.id } + 1

        tasksData.add(position, newTask)
        listener.onListChange(tasksData.size)

        notifyDataSetChanged()
    }

}