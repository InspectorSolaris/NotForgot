package com.example.mobilelab.model

import android.content.Context
import androidx.room.Room
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.database.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repsoitory(
    context: Context
) {

    private val taskDatabase: TaskDatabase = Room.databaseBuilder(
        context,
        TaskDatabase::class.java,
        TASK_DATABASE
    ).build()

    companion object {
        private const val TASK_DATABASE = "taskDatabase"
    }

    fun getAllTaskData(
        onGetTaskData: (taskData: ArrayList<TaskData>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val taskData = withContext(Dispatchers.IO) { taskDatabase.taskDataDao().getAllTaskData() }

            onGetTaskData(taskData)
        }
    }

    fun saveNewTaskData(
        newTaskData: TaskData,
        onSave: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            taskDatabase.taskDataDao().addTaskData(newTaskData)

            onSave()
        }
    }

}