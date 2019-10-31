package com.example.mobilelab.model

import android.content.Context
import androidx.room.Room
import com.example.mobilelab.model.data.TaskData
import com.example.mobilelab.model.data.UserData
import com.example.mobilelab.model.database.AppDatabase
import com.example.mobilelab.model.server.NotForgotAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    context: Context
) {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var appDatabase: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    companion object {
        private const val APP_DATABASE_NAME = "appDatabase"
        private const val API_BASE_URL = "http://practice.mobile.kreosoft.ru/api/"
    }

//    fun getAllTaskDataFromDB(
//        user: String,
//        onGetTaskData: (taskData: ArrayList<TaskData>) -> Unit
//    ) {
//        GlobalScope.launch(Dispatchers.Main) {
//            val taskData = withContext(Dispatchers.IO) {
//                appDatabase.taskDataDao().getAllTaskData(user)
//            }
//
//            onGetTaskData(ArrayList(taskData))
//        }
//    }
//
//    fun saveNewTaskDataFromDB(
//        newTaskData: TaskData,
//        onSave: () -> Unit
//    ) {
//        GlobalScope.launch(Dispatchers.IO) {
//            appDatabase.taskDataDao().addTaskData(newTaskData)
//
//            onSave()
//        }
//    }
//
//    fun getAllUserDataFromDB(
//        onGetUserData: (userData: ArrayList<UserData>) -> Unit
//    ) {
//        GlobalScope.launch(Dispatchers.Main) {
//            val userData = withContext(Dispatchers.IO) {
//                appDatabase.userDataDao().getAllUserData()
//            }
//
//            onGetUserData(ArrayList(userData))
//        }
//    }
//
//    fun saveNewUserDataFromDB(
//        newUserData: UserData,
//        onSave: () -> Unit
//    ) {
//        GlobalScope.launch(Dispatchers.IO) {
//            appDatabase.userDataDao().addUserData(newUserData)
//
//            onSave()
//        }
//    }

}