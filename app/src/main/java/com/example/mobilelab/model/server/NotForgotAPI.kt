package com.example.mobilelab.model.server

import com.example.mobilelab.model.server.array.ArrayOfCategories
import com.example.mobilelab.model.server.array.ArrayOfPriorities
import com.example.mobilelab.model.server.array.ArrayOfTasks
import com.example.mobilelab.model.server.form.CategoryForm
import com.example.mobilelab.model.server.form.TaskForm
import com.example.mobilelab.model.server.task.Category
import com.example.mobilelab.model.server.task.Task
import com.example.mobilelab.model.server.user.Token
import com.example.mobilelab.model.server.user.User
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.model.server.user.UserRegistrationForm
import retrofit2.Call
import retrofit2.http.*

interface NotForgotAPI {

    @POST("/register")
    fun postRegister(
        @Header("Accept") accept: String,
        @Body userRegistrationForm: UserRegistrationForm
    ): Call<User>

    @POST("/login")
    fun postLogin(
        @Header("Accept") accept: String,
        @Body userLoginForm: UserLoginForm
    ): Call<Token>


    @GET("/categories")
    fun getCategories(
        @Header("Accept") accept: String
    ): Call<ArrayOfCategories>

    @GET("/priorities")
    fun getPriorities(
        @Header("Accept") accept: String
    ): Call<ArrayOfPriorities>

    @GET("/tasks")
    fun getTasks(
        @Header("Accept") accept: String
    ): Call<ArrayOfTasks>


    @POST("/categories")
    fun postCategories(
        @Header("Accept") accept: String,
        @Body categoryForm: CategoryForm
    ): Call<Category>

    @POST("/tasks")
    fun postTasks(
        @Header("Accept") accept: String,
        @Body taskForm: TaskForm
    ): Call<Task>


    @PATCH("/tasks/{id}")
    fun patchTasks(
        @Header("Accept") accept: String,
        @Path("id") id: Int,
        @Body taskForm: TaskForm
    ): Call<Task>

    @DELETE("/tasks/{id}")
    fun deleteTasks(
        @Header("Accept") accept: String,
        @Path("id") id: Int
    )

}