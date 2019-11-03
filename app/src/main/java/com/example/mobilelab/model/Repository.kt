package com.example.mobilelab.model

import com.example.mobilelab.model.database.AppDatabase
import com.example.mobilelab.model.server.NotForgotAPI
import com.example.mobilelab.model.server.form.CategoryForm
import com.example.mobilelab.model.server.form.TaskForm
import com.example.mobilelab.model.server.user.Token
import com.example.mobilelab.model.server.user.User
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.model.server.user.UserRegistrationForm
import com.example.mobilelab.model.taskData.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    private val retrofit: Retrofit
    private val notForgotAPI: NotForgotAPI
    private lateinit var appDatabase: AppDatabase

    private const val API_BASE_URL = "http://practice.mobile.kreosoft.ru/api/"
    private const val TOKEN_PREFIX = "Bearer "
    const val APP_DATABASE_NAME = "AppDatabase"

    private var internetConnected = true
    private var categoriesData: ArrayList<Category> = arrayListOf()
    private var prioritiesData: ArrayList<Priority> = arrayListOf()
    private var tasksData: ArrayList<Task> = arrayListOf()

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        notForgotAPI = retrofit.create(NotForgotAPI::class.java)
    }

    fun setAppDatabase(
        appDatabase: AppDatabase
    ) {
        this.appDatabase = appDatabase
    }

    fun getCategoriesData(): ArrayList<Category> {
        return categoriesData
    }

    fun getPrioritiesData(): ArrayList<Priority> {
        return prioritiesData
    }

    fun getTasksData(): ArrayList<Task> {
        return tasksData
    }

    fun loginUser(
        userLoginForm: UserLoginForm,
        onFailure: () -> Unit,
        onResponse: (Response<Token>) -> Unit
    ) {
        val request = notForgotAPI.postLogin(userLoginForm)

        request.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                onFailure()
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                onResponse(response)
            }
        })
    }

    fun registerUser(
        userRegistrationForm: UserRegistrationForm,
        onFailure: () -> Unit,
        onResponse: (Response<User>) -> Unit
    ) {
        val request = notForgotAPI.postRegister(userRegistrationForm)

        request.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                onFailure()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                onResponse(response)
            }
        })
    }

    fun getCategories(
        token: String
    ) {
        val categoryRequest = notForgotAPI.getCategories(TOKEN_PREFIX + token)

        categoryRequest.enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                internetConnected = false

                GlobalScope.launch(Dispatchers.IO) {
                    categoriesData = ArrayList(appDatabase.categoryDao().getCategories())
                }
            }

            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if(response.body() != null) {
                    categoriesData = ArrayList(response.body()!!)
                    categoriesData.sortBy { it.id }
                }
            }
        })
    }

    fun getPriorities(
        token: String
    ) {
        val priorityRequest = notForgotAPI.getPriorities(TOKEN_PREFIX + token)

        priorityRequest.enqueue(object : Callback<List<Priority>> {
            override fun onFailure(call: Call<List<Priority>>, t: Throwable) {
                internetConnected = false

                GlobalScope.launch(Dispatchers.IO) {
                    prioritiesData = ArrayList(appDatabase.priorityDao().getPriorities())
                }
            }

            override fun onResponse(call: Call<List<Priority>>, response: Response<List<Priority>>) {
                if(response.body() != null) {
                    prioritiesData = ArrayList(response.body()!!)
                    prioritiesData.sortBy { it.id }
                }
            }
        })
    }

    fun getTasks(
        token: String,
        onResponse: () -> Unit
    ) {
        val taskRequest = notForgotAPI.getTasks(TOKEN_PREFIX + token)

        taskRequest.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                internetConnected = false

                GlobalScope.launch(Dispatchers.IO) {
                    tasksData = ArrayList(appDatabase.taskDao().getTasks())
                }
            }

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if(response.body() != null) {
                    tasksData = ArrayList(response.body()!!)

                    tasksData.filter { it.category == null }.forEach { it.category = Category(-1, "NULL") }
                    tasksData.filter { it.priority == null }.forEach { it.priority = Priority(-1, "NULL", "#000000") }
                    tasksData.sortBy { it.category?.id }

                    onResponse()
                }
            }
        })
    }

    fun postCategory(
        token: String,
        categoryForm: CategoryForm,
        onResponse: () -> Unit
    ) {
        val request = notForgotAPI.postCategory(TOKEN_PREFIX + token, categoryForm)

        request.enqueue(object : Callback<Category> {
            override fun onFailure(call: Call<Category>, t: Throwable) {}

            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if(response.body() != null) {
                    val category = response.body()!!

                    categoriesData.add(category)

                    onResponse()
                }
            }
        })
    }

    fun postTask(
        token: String,
        taskForm: TaskForm,
        onResponse: () -> Unit
    ) {
        val request = notForgotAPI.postTask(TOKEN_PREFIX + token, taskForm)

        request.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {}

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.body() != null) {
                    val task = response.body()!!
                    val position = tasksData.indexOfLast { it.category?.id == task.category?.id } + 1

                    tasksData.add(position, task)

                    onResponse()
                }
            }

        })
    }

    fun patchTask(
        token: String,
        id: Int,
        taskForm: TaskForm,
        onResponse: () -> Unit
    ) {
        val request = notForgotAPI.patchTask(TOKEN_PREFIX + token, id, taskForm)

        request.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {}

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.body() != null) {
                    val task = response.body()!!
                    val position = tasksData.indexOfFirst { it.id == task.id }

                    tasksData[position] = task

                    onResponse()
                }
            }
        })
    }

    fun deleteTask(
        token: String,
        id: Int,
        onResponse: () -> Unit
    ) {
        val request = notForgotAPI.deleteTask(TOKEN_PREFIX + token, id)

        request.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val position = tasksData.indexOfFirst { it.id == id }

                if(response.code() == 200) {
                    tasksData.removeAt(position)

                    onResponse()
                }
            }
        })
    }

    fun appDataBaseRefreshCategories() {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.categoryDao().deleteCategories(
                appDatabase.categoryDao().getCategories()
            )

            appDatabase.categoryDao().postCategories(categoriesData)
        }
    }

    fun appDataBaseRefreshPriorities() {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.priorityDao().deletePriorities(
                appDatabase.priorityDao().getPriorities()
            )

            appDatabase.priorityDao().postPriorities(prioritiesData)
        }
    }

    fun appDataBaseRefreshTasks() {
        GlobalScope.launch(Dispatchers.IO) {
            appDatabase.taskDao().deleteTasks(
                appDatabase.taskDao().getTasks()
            )

            appDatabase.taskDao().postTasks(tasksData)
        }
    }

}