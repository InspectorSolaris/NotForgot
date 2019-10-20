package com.example.mobilelab.model

import android.content.Context
import androidx.room.Room
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.data.UserData
import com.example.mobilelab.model.database.task.TaskDatabase
import com.example.mobilelab.model.database.user.UserDatabase
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
        TASK_DATABASE_NAME
    ).build()

    private val userDatabase: UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        USER_DATABASE_NAME
    ).build()

    companion object {
        private const val TASK_DATABASE_NAME = "taskDatabase"
        private const val USER_DATABASE_NAME = "userDatabase"
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

    fun getAllUserData(
        onGetUserData: (userData: ArrayList<UserData>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val userData = withContext(Dispatchers.IO) { userDatabase.userDataDao().getAllUserData() }

            onGetUserData(userData)
        }
    }

    fun saveNewUserData(
        newUserData: UserData,
        onSave: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            userDatabase.userDataDao().addUserData(newUserData)

            onSave()
        }
    }

}