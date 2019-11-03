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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    private val retrofit: Retrofit
    private val notForgotAPI: NotForgotAPI
    private lateinit var appDatabase: AppDatabase

    private const val API_BASE_URL = "http://practice.mobile.kreosoft.ru/api/"
    private const val TOKEN_PREFIX = "Bearer "
    const val appDatabaseName = "AppDatabase"

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

    private fun getCategories(
        token: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val categories = appDatabase.categoryDao().getCategories()

            categories.forEach {
                if(categoriesData.indexOfFirst { c -> it.id == c.id } == -1) {
                    categoriesData.add(it)
                }
            }

            categoriesData.sortBy { it.id }
        }

        val categoryRequest = notForgotAPI.getCategories(TOKEN_PREFIX + token)

        categoryRequest.enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if(response.body() != null) {
                    val categories = ArrayList(response.body()!!)

                    GlobalScope.launch(Dispatchers.IO) {
                        categories.forEach {
                            if(categoriesData.indexOfFirst { c -> it.id == c.id } == -1) {
                                categoriesData.add(it)
                            }
                        }

                        categoriesData.sortBy { it.id }
                        appDatabase.categoryDao().postCategory(categoriesData)
                    }
                }
            }
        })
    }

    private fun getPriorities(
        token: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val priorities = appDatabase.priorityDao().getPriorities()

            priorities.forEach {
                if(prioritiesData.indexOfFirst { c -> it.id == c.id } == -1) {
                    prioritiesData.add(it)
                }
            }

            prioritiesData.sortBy { it.id }
        }

        val priorityRequest = notForgotAPI.getPriorities(TOKEN_PREFIX + token)

        priorityRequest.enqueue(object : Callback<List<Priority>> {
            override fun onFailure(call: Call<List<Priority>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Priority>>, response: Response<List<Priority>>) {
                if(response.body() != null) {
                    val priorities = ArrayList(response.body()!!)

                    GlobalScope.launch(Dispatchers.IO) {
                        priorities.forEach {
                            if(prioritiesData.indexOfFirst { c -> it.id == c.id } == -1) {
                                prioritiesData.add(it)
                            }
                        }

                        prioritiesData.sortBy { it.id }
                        appDatabase.priorityDao().postPriorities(priorities)
                    }
                }
            }
        })
    }

    fun getTasks(
        token: String,
        onFailure: () -> Unit,
        onResponse: () -> Unit
    ) {
        val categories = GlobalScope.async {
            getCategories(token)
        }
        val priorities = GlobalScope.async {
            getPriorities(token)
        }

        GlobalScope.launch(Dispatchers.IO) {
            val tasksExt = appDatabase.taskDao().getTasks(token)
            val tasks = tasksExt.map {
                Task(
                    it.id,
                    it.title,
                    it.description,
                    it.done,
                    it.created,
                    it.deadline,
                    Category(it.category_id, ""),
                    Priority(it.priority_id, "", "")
                )
            }

            tasks.forEach {
                if(tasksData.indexOfFirst { c -> it.id == c.id } == -1) {
                    tasksData.add(it)
                }
            }

            tasksData.sortBy { it.id }
        }

        val taskRequest = notForgotAPI.getTasks(TOKEN_PREFIX + token)

        taskRequest.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onFailure()
            }

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if(response.body() != null) {
                    val tasks = ArrayList(response.body()!!)

                    GlobalScope.launch(Dispatchers.IO) {
                        tasks.forEach {
                            if(tasksData.indexOfFirst { c -> it.id == c.id } == -1) {
                                tasksData.add(it)
                            }
                        }

                        tasksData.sortBy { it.id }
                        appDatabase.taskDao().postTask(tasksData.map {
                            TaskExt(
                                token,
                                it.id,
                                it.title,
                                it.description,
                                it.done,
                                it.created,
                                it.deadline,
                                it.category!!.id,
                                it.priority!!.id
                            )
                        })

                        categories.await()
                        priorities.await()

                        tasksData.filter { it.category == null }.forEach { it.category = Category(-1, "NULL") }
                        tasksData.filter { it.priority == null }.forEach { it.priority = Priority(-1, "NULL", "#000000") }
                        tasksData.filter { it.category!!.id != -1 }.forEach { it.category = categoriesData.find { c -> it.category!!.id == c.id } }
                        tasksData.filter { it.priority!!.id != -1 }.forEach { it.priority = prioritiesData.find { c -> it.priority!!.id == c.id } }
                        tasksData.sortBy { T -> T.category?.id }

                        launch(Dispatchers.Main) {
                            onResponse()
                        }
                    }
                }
            }
        })
    }

    fun postCategory(
        token: String,
        categoryForm: CategoryForm,
        onFailure: (Call<Category>, Throwable) -> Unit,
        onResponse: (Call<Category>, Response<Category>) -> Unit
    ) {
        val request = notForgotAPI.postCategory(TOKEN_PREFIX + token, categoryForm)

        request.enqueue(object : Callback<Category> {
            override fun onFailure(call: Call<Category>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if(response.body() != null) {
                    categoriesData.add(response.body()!!)
                }

                onResponse(call, response)
            }

        })
    }

    fun postTask(
        token: String,
        taskForm: TaskForm,
        onFailure: (Call<Task>, Throwable) -> Unit,
        onResponse: (Call<Task>, Response<Task>) -> Unit
    ) {
        val request = notForgotAPI.postTask(TOKEN_PREFIX + token, taskForm)

        request.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.body() != null) {
                    val taskData = response.body()!!
                    val position = tasksData.indexOfLast { it.category?.id == taskData.category?.id } + 1

                    tasksData.add(position, taskData)
                }

                onResponse(call, response)
            }

        })
    }

    fun patchTask(
        token: String,
        id: Int,
        taskForm: TaskForm,
        onFailure: (Call<Task>, Throwable) -> Unit,
        onResponse: (Call<Task>, Response<Task>) -> Unit
    ) {
        val request = notForgotAPI.patchTask(TOKEN_PREFIX + token, id, taskForm)

        request.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.body() != null) {
                    val taskData = response.body()!!
                    val position = tasksData.indexOfFirst { it.id == taskData.id }

                    tasksData[position] = taskData
                }

                onResponse(call, response)
            }

        })
    }

    fun deleteTask(
        token: String,
        id: Int,
        onFailure: (Call<Void>, Throwable) -> Unit,
        onResponse: (Call<Void>, Response<Void>) -> Unit
    ) {
        val request = notForgotAPI.deleteTask(TOKEN_PREFIX + token, id)

        request.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                onFailure(call, t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 200) {
                    val position = tasksData.indexOfFirst { it.id == id }

                    tasksData.removeAt(position)
                }

                onResponse(call, response)
            }

        })
    }

}