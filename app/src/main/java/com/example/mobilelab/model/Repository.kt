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

    private fun mergeCategories(
        categories: List<Category>
    ) {
        categories.forEach {
            if(categoriesData.indexOfFirst { c -> it.id == c.id } == -1) {
                categoriesData.add(it)
            }
        }
    }

    fun getCategories(
        token: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val categories = appDatabase.categoryDao().getCategories()

            mergeCategories(categories)

            categoriesData.sortBy { it.id }
        }

        val categoryRequest = notForgotAPI.getCategories(TOKEN_PREFIX + token)

        categoryRequest.enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {}

            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if(response.body() != null) {
                    val categories = ArrayList(response.body()!!)

                    GlobalScope.launch(Dispatchers.IO) {
                        mergeCategories(categories)

                        categoriesData.sortBy { it.id }
                        appDatabase.categoryDao().postCategory(categoriesData)
                    }
                }
            }
        })
    }

    private fun mergePriorities(
        priorities: List<Priority>
    ) {
        priorities.forEach {
            if(prioritiesData.indexOfFirst { c -> it.id == c.id } == -1) {
                prioritiesData.add(it)
            }
        }
    }

    fun getPriorities(
        token: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val priorities = appDatabase.priorityDao().getPriorities()

            mergePriorities(priorities)

            prioritiesData.sortBy { it.id }
        }

        val priorityRequest = notForgotAPI.getPriorities(TOKEN_PREFIX + token)

        priorityRequest.enqueue(object : Callback<List<Priority>> {
            override fun onFailure(call: Call<List<Priority>>, t: Throwable) {}

            override fun onResponse(call: Call<List<Priority>>, response: Response<List<Priority>>) {
                if(response.body() != null) {
                    val priorities = ArrayList(response.body()!!)

                    GlobalScope.launch(Dispatchers.IO) {
                        mergePriorities(priorities)

                        prioritiesData.sortBy { it.id }
                        appDatabase.priorityDao().postPriorities(priorities)
                    }
                }
            }
        })
    }

    private fun mergeTasks(
        tasks: List<Task>
    ) {
        tasks.forEach {
            if(tasksData.indexOfFirst { c -> it.id == c.id } == -1) {
                tasksData.add(it)
            }
        }
    }

    fun getTasks(
        token: String,
        onResponse: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val tasks = appDatabase.taskDao().getTasks()

            mergeTasks(tasks)

            tasksData.sortBy { it.category?.id }

            launch(Dispatchers.Main) {
                onResponse()
            }
        }

        val taskRequest = notForgotAPI.getTasks(TOKEN_PREFIX + token)

        taskRequest.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if(response.body() != null) {
                    val tasks = ArrayList(response.body()!!)

                    GlobalScope.launch(Dispatchers.IO) {
                        mergeTasks(tasks)

                        tasksData.filter { it.category == null }.forEach { it.category = Category(-1, "NULL") }
                        tasksData.filter { it.priority == null }.forEach { it.priority = Priority(-1, "NULL", "#000000") }
                        tasksData.sortBy { it.category?.id }

                        appDatabase.taskDao().postTask(tasksData)

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
        onResponse: () -> Unit
    ) {
        val request = notForgotAPI.postCategory(TOKEN_PREFIX + token, categoryForm)

        request.enqueue(object : Callback<Category> {
            override fun onFailure(call: Call<Category>, t: Throwable) {
                GlobalScope.launch(Dispatchers.IO) {
                    val category = Category(0, categoryForm.name)

                    categoriesData.add(category)
                    appDatabase.categoryDao().postCategory(
                        arrayListOf(
                            category
                        )
                    )

                    launch(Dispatchers.Main) {
                        onResponse()
                    }
                }
            }

            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if(response.body() != null) {
                    val category = response.body()!!

                    GlobalScope.launch(Dispatchers.IO) {
                        categoriesData.add(category)
                        appDatabase.categoryDao().postCategory(
                            arrayListOf(category)
                        )

                        launch(Dispatchers.Main) {
                            onResponse()
                        }
                    }
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
            override fun onFailure(call: Call<Task>, t: Throwable) {
                GlobalScope.launch(Dispatchers.IO) {
                    val task = Task(
                        0,
                        taskForm.title,
                        taskForm.description,
                        taskForm.done,
                        System.currentTimeMillis() / 1000L,
                        taskForm.deadline,
                        categoriesData.find { it.id == taskForm.category_id },
                        prioritiesData.find { it.id == taskForm.priority_id }
                    )
                    val position = tasksData.indexOfLast { it.category?.id == task.category?.id } + 1

                    tasksData.add(position, task)
                    appDatabase.taskDao().postTask(
                        arrayListOf(
                            task
                        )
                    )

                    launch(Dispatchers.Main) {
                        onResponse()
                    }
                }
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.body() != null) {
                    val task = response.body()!!
                    val position = tasksData.indexOfLast { it.category?.id == task.category?.id } + 1

                    GlobalScope.launch(Dispatchers.IO) {
                        tasksData.add(position, task)
                        appDatabase.taskDao().postTask(
                            arrayListOf(task)
                        )

                        launch(Dispatchers.Main) {
                            onResponse()
                        }
                    }
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
            override fun onFailure(call: Call<Task>, t: Throwable) {
                GlobalScope.launch(Dispatchers.IO) {
                    val taskOld = tasksData.find { it.id == id }!!
                    val task = Task(
                        taskOld.id,
                        taskForm.title,
                        taskForm.description,
                        taskForm.done,
                        taskOld.created,
                        taskForm.deadline,
                        categoriesData.find { it.id == taskForm.category_id },
                        prioritiesData.find { it.id == taskForm.priority_id }
                    )
                    val position = tasksData.indexOfFirst { it.id == task.id }

                    tasksData[position] = task
                    appDatabase.taskDao().patchTask(task)

                    launch(Dispatchers.Main) {
                        onResponse()
                    }
                }
            }

            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if(response.body() != null) {
                    val task = response.body()!!
                    val position = tasksData.indexOfFirst { it.id == task.id }

                    GlobalScope.launch(Dispatchers.IO) {
                        tasksData[position] = task
                        appDatabase.taskDao().patchTask(task)

                        launch(Dispatchers.Main) {
                            onResponse()
                        }
                    }
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
            override fun onFailure(call: Call<Void>, t: Throwable) {
                GlobalScope.launch(Dispatchers.IO) {
                    val position = tasksData.indexOfFirst { it.id == id }

                    appDatabase.taskDao().deleteTask(tasksData[position])
                    tasksData.removeAt(position)

                    launch(Dispatchers.Main) {
                        onResponse()
                    }
                }
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.code() == 200) {
                    val position = tasksData.indexOfFirst { it.id == id }

                    GlobalScope.launch(Dispatchers.IO) {
                        appDatabase.taskDao().deleteTask(tasksData[position])
                        tasksData.removeAt(position)

                        launch(Dispatchers.Main) {
                            onResponse()
                        }
                    }

                }
            }
        })
    }

    fun clearTasksFromAppDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            tasksData.forEach {
                appDatabase.taskDao().deleteTask(it)
            }
        }
    }

}