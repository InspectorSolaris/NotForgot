package com.example.mobilelab.model

import android.content.Context
import androidx.room.Room
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.data.UserData
import com.example.mobilelab.model.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(
    context: Context
) {

    private val appDatabase: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    companion object {
        private const val APP_DATABASE_NAME = "appDatabase"
    }

    fun getAllTaskData(
        user: String,
        onGetTaskData: (taskData: ArrayList<TaskData>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val taskData = withContext(Dispatchers.IO) {
                appDatabase.taskDataDao().getAllTaskData(user)
            }

            onGetTaskData(taskData)
        }
    }

    fun saveNewTaskData(
        newTaskData: TaskData,
        onSave: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.taskDataDao().addTaskData(newTaskData)

            onSave()
        }
    }

    fun getAllUserData(
        onGetUserData: (userData: ArrayList<UserData>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val userData = withContext(Dispatchers.IO) {
                appDatabase.userDataDao().getAllUserData()
            }

            onGetUserData(userData)
        }
    }

    fun saveNewUserData(
        newUserData: UserData,
        onSave: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.userDataDao().addUserData(newUserData)

            onSave()
        }
    }

}