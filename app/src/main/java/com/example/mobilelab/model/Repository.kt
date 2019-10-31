package com.example.mobilelab.model

import android.content.Context
import androidx.room.Room
import com.example.mobilelab.model.database.AppDatabase
import com.example.mobilelab.model.server.NotForgotAPI
import com.example.mobilelab.model.server.array.ArrayOfCategories
import com.example.mobilelab.model.server.array.ArrayOfPriorities
import com.example.mobilelab.model.server.array.ArrayOfTasks
import com.example.mobilelab.model.server.user.Token
import com.example.mobilelab.model.server.user.User
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.model.server.user.UserRegistrationForm
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Priority
import com.example.mobilelab.model.taskData.Task
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    context: Context
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val notForgotAPI = retrofit.create(NotForgotAPI::class.java)
    private var appDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    companion object {
        private const val APP_DATABASE_NAME = "appDatabase"
        private const val API_BASE_URL = "http://practice.mobile.kreosoft.ru/api/"
        private const val BEARER = "Bearer"
    }

    fun getCategories(
        token: String,
        callbackOnFailure: (Call<ArrayOfCategories>, Throwable) -> Unit,
        callbackOnResponse: (ArrayList<Category>) -> Unit
    ) {
        val categoryRequest = notForgotAPI.getCategories(token)

        categoryRequest.enqueue(object : Callback<ArrayOfCategories> {
            override fun onFailure(call: Call<ArrayOfCategories>, t: Throwable) {
                callbackOnFailure(call, t)
            }

            override fun onResponse(call: Call<ArrayOfCategories>, response: Response<ArrayOfCategories>) {
                if(response.body() != null) {
                    callbackOnResponse(response.body()!!.categories)
                }
            }

        })
    }

    fun getPriorities(
        token: String,
        callbackOnFailure: (Call<ArrayOfPriorities>, Throwable) -> Unit,
        callbackOnResponse: (ArrayList<Priority>) -> Unit
    ) {
        val priorityRequest = notForgotAPI.getPriorities(token)

        priorityRequest.enqueue(object : Callback<ArrayOfPriorities> {
            override fun onFailure(call: Call<ArrayOfPriorities>, t: Throwable) {
                callbackOnFailure(call, t)
            }

            override fun onResponse(call: Call<ArrayOfPriorities>, response: Response<ArrayOfPriorities>) {
                if(response.body() != null) {
                    callbackOnResponse(response.body()!!.priorities)
                }
            }

        })
    }

    fun getTasks(
        token: String,
        callbackOnFailure: (Call<ArrayOfTasks>, Throwable) -> Unit,
        callbackOnResponse: (ArrayList<Task>) -> Unit
    ) {
        val taskRequest = notForgotAPI.getTasks(token)

        taskRequest.enqueue(object : Callback<ArrayOfTasks> {
            override fun onFailure(call: Call<ArrayOfTasks>, t: Throwable) {
                callbackOnFailure(call, t)
            }

            override fun onResponse(call: Call<ArrayOfTasks>, response: Response<ArrayOfTasks>) {
                if(response.body() != null) {
                    callbackOnResponse(response.body()!!.tasks)
                }
            }

        })
    }

    fun loginUser(
        userLoginForm: UserLoginForm,
        onFailure: (Call<Token>, Throwable) -> Unit,
        onResponse: (Call<Token>, Response<Token>) -> Unit
    ) {
        val request = notForgotAPI.postLogin(userLoginForm)

        request.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                onResponse(call, response)
            }

        })
    }

    fun registerUser(
        userRegistrationForm: UserRegistrationForm,
        onFailure: (Call<User>, Throwable) -> Unit,
        onResponse: (Call<User>, Response<User>) -> Unit
    ) {
        val request = notForgotAPI.postRegister(userRegistrationForm)

        request.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                onResponse(call, response)
            }

        })
    }

}